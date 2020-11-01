package eu.mcone.gameapi.kit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.inventory.kit.KitsInventory;
import eu.mcone.gameapi.listener.kit.KitListener;
import eu.mcone.gameapi.listener.kit.KitSortListener;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class GameKitManager implements KitManager {

    private static final MongoCollection<Document> MODIFIED_KITS_COLLECTION = CoreSystem.getInstance().getMongoDB().getCollection("gameapi_modified_kits");

    @Getter
    private final boolean clearInvOnKitSet, chooseKitsForServerLifetime;
    private final GameAPIPlugin system;
    private final GamePlugin plugin;

    @Getter
    private final List<Kit> kits;
    private final Map<UUID, List<ModifiedKit>> modifiedKits;
    private final Map<UUID, Set<Kit>> playerKits;

    @Setter
    @Getter
    private Kit defaultKit;

    public GameKitManager(GameAPIPlugin system, GamePlugin plugin) {
        this.clearInvOnKitSet = plugin.hasOption(Option.KIT_MANAGER_CLEAR_INVENTORY_ON_KIT_SET);
        this.chooseKitsForServerLifetime = plugin.hasOption(Option.KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME);

        this.system = system;
        this.plugin = plugin;
        this.kits = new ArrayList<>();
        this.modifiedKits = new HashMap<>();
        this.playerKits = new HashMap<>();

        reload();

        system.sendConsoleMessage("§aLoading KitManager...");
        system.registerEvents(new KitSortListener(this), new KitListener(this));
    }

    @Override
    public void reload() {
        modifiedKits.clear();

        for (Document kit : MODIFIED_KITS_COLLECTION.find(eq("gamemode", plugin.getPluginSlug()))) {
            UUID uuid = UUID.fromString(kit.getString("uuid"));
            ModifiedKit customkit = new ModifiedKit(
                    kit.getLong("lastUpdated"),
                    kit.getString("name"),
                    calculateReplacedItemSlots(kit.get("items", Document.class))
            );

            if (modifiedKits.containsKey(uuid)) {
                modifiedKits.get(uuid).add(customkit);
            } else {
                modifiedKits.put(uuid, new ArrayList<>(Collections.singleton(customkit)));
            }
        }
    }

    @Override
    public void registerKits(Kit... kit) {
        kits.addAll(Arrays.asList(kit));
    }

    @Override
    public void removeKit(String name) {
        kits.removeIf(kit -> kit.getName().equals(name));
    }

    @Override
    public Kit getKit(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equals(name)) {
                return kit;
            }
        }
        return null;
    }

    public boolean hasKit(UUID uuid, String name) {
        return hasKit(uuid, getKit(name));
    }

    public boolean hasKit(UUID uuid, Kit kit) {
        return playerKits.containsKey(uuid) && playerKits.get(uuid).contains(kit);
    }

    public boolean addKit(GamePlayer gp, Kit kit) {
        UUID uuid = gp.getCorePlayer().getUuid();

        if (playerKits.containsKey(uuid)) {
            return playerKits.get(uuid).add(kit);
        } else {
            playerKits.put(uuid, new HashSet<>(Collections.singleton(kit)));
            return true;
        }
    }

    public boolean removeKit(GamePlayer gp, Kit kit) {
        return playerKits.getOrDefault(gp.getCorePlayer().getUuid(), Collections.emptySet()).remove(kit);
    }

    public void setKit(Kit kit, Player p) {
        GameAPIPlayer gp = system.getGamePlayer(p);
        Kit currentKit = gp.getCurrentKit();

        if (clearInvOnKitSet && currentKit != null) {
            for (ItemStack item : currentKit.getKitItems().values()) {
                p.getInventory().remove(item);
            }

            p.getInventory().setArmorContents(null);
        }

        gp.saveCurrentKit(kit);

        Map<Integer, ItemStack> items = calculateItems(kit, p);
        List<ItemStack> addLater = new ArrayList<>();

        for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
            if (p.getInventory().getItem(item.getKey()) != null && !p.getInventory().getItem(item.getKey()).getType().equals(Material.AIR)) {
                addLater.add(p.getInventory().getItem(item.getKey()));
            }

            p.getInventory().setItem(item.getKey(), item.getValue());
        }

        p.getInventory().addItem(addLater.toArray(new ItemStack[0]));
    }

    @Override
    public void openKitsInventory(Player p, Runnable onBackClick) {
        new KitsInventory(p, this, onBackClick);
    }

    @Override
    public ModifiedKit getModifiedKit(Player p, String name) {
        return getModifiedKit(name, modifiedKits.getOrDefault(p.getUniqueId(), Collections.emptyList()));
    }

    @Override
    public boolean hasKitModified(Player p, String name) {
        return getModifiedKit(p, name) != null;
    }

    public void modifyKit(Player p, Kit kit, Map<ItemStack, Integer> items) {
        Map<Integer, Integer> customItems = new HashMap<>();

        for (Map.Entry<Integer, ItemStack> kitItem : kit.getKitItems().entrySet()) {
            if (items.containsKey(kitItem.getValue())) {
                customItems.put(kitItem.getKey(), items.get(kitItem.getValue()));
            }
        }

        ModifiedKit modifiedKit = new ModifiedKit(System.currentTimeMillis() / 1000, kit.getName(), customItems);
        if (modifiedKits.containsKey(p.getUniqueId())) {
            ModifiedKit old = getModifiedKit(kit.getName(), modifiedKits.get(p.getUniqueId()));
            if (old != null) {
                modifiedKits.get(p.getUniqueId()).remove(old);
            }

            modifiedKits.get(p.getUniqueId()).add(modifiedKit);
        } else {
            modifiedKits.put(p.getUniqueId(), new ArrayList<>(Collections.singleton(modifiedKit)));
        }

        MODIFIED_KITS_COLLECTION.updateOne(
                combine(
                        eq("uuid", p.getUniqueId().toString()),
                        eq("gamemode", plugin.getPluginSlug()),
                        eq("name", modifiedKit.getName())
                ),
                combine(
                        set("lastUpdated", modifiedKit.getLastUpdated()),
                        set("items", modifiedKit.getCustomItems())
                ),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public Map<Integer, ItemStack> calculateItems(Kit kit, Player player) {
        ModifiedKit modifiedKit = system.getGamePlayer(player).getModifiedKit(kit);

        if (modifiedKit != null) {
            Map<Integer, ItemStack> items = new HashMap<>();

            for (Map.Entry<Integer, ItemStack> entry : kit.getKitItems().entrySet()) {
                if (modifiedKit.calculateCustomItems().containsKey(entry.getKey())) {
                    items.put(modifiedKit.calculateCustomItems().get(entry.getKey()), entry.getValue());
                }
            }

            return items;
        }

        return kit.getKitItems();
    }

    private static ModifiedKit getModifiedKit(String name, List<ModifiedKit> customKitList) {
        for (ModifiedKit customKit : customKitList) {
            if (customKit.getName().equals(name)) {
                return customKit;
            }
        }

        return null;
    }

    private static Map<Integer, Integer> calculateReplacedItemSlots(Map<String, Object> items) {
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : items.entrySet()) {
            result.put(Integer.parseInt(entry.getKey()), (Integer) entry.getValue());
        }

        return result;
    }

}
