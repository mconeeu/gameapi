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

    private static final MongoCollection<Document> PLAYER_KITS_COLLECTION = CoreSystem.getInstance().getMongoDB().getCollection("kits");
    private static final MongoCollection<Document> KIT_COLLECTION = CoreSystem.getInstance().getMongoDB().getCollection("custom_kits");

    @Getter
    private final boolean clearInvOnKitSet, applyKitsOnce;
    private final GameAPIPlugin system;
    private final GamePlugin plugin;

    @Getter
    private final List<Kit> kits;
    private final Map<UUID, String> currentKits;
    private final Map<UUID, List<String>> playerKits;
    private final Map<UUID, List<ModifiedKit>> customKits;

    @Setter @Getter
    private Kit defaultKit;

    public GameKitManager(GameAPIPlugin system, GamePlugin plugin, Option... options) {
        List<Option> optionList = Arrays.asList(options);

        this.clearInvOnKitSet = optionList.contains(Option.KIT_MANAGER_CLEAR_INVENTORY_ON_KIT_SET);
        this.applyKitsOnce = optionList.contains(Option.KIT_MANAGER_APPLY_KITS_ONCE);
        this.system = system;
        this.plugin = plugin;

        this.kits = new ArrayList<>();
        this.currentKits = new HashMap<>();
        this.playerKits = new HashMap<>();
        this.customKits = new HashMap<>();

        system.sendConsoleMessage("§aLoading KitManager...");
        system.registerEvents(new KitSortListener(this), new KitListener(this));
        reload();
    }

    @Override
    public void reload() {
        playerKits.clear();
        customKits.clear();

        for (Document kit : KIT_COLLECTION.find()) {
            if (kit.getString("gamemode").equals(plugin.getGamemode().name())) {
                UUID uuid = UUID.fromString(kit.getString("uuid"));
                ModifiedKit customkit = new ModifiedKit(
                        kit.getLong("lastUpdated"),
                        kit.getString("name"),
                        calculateReplacedItemSlots(kit.get("items", Document.class))
                );

                if (customKits.containsKey(uuid)) {
                    customKits.get(uuid).add(customkit);
                } else {
                    customKits.put(uuid, new ArrayList<>(Collections.singleton(customkit)));
                }
            }
        }

        for (Document playerEntry : PLAYER_KITS_COLLECTION.find()) {
            UUID uuid = UUID.fromString(playerEntry.getString("uuid"));

            if (playerEntry.get(plugin.getGamemode().toString()) != null) {
                String currentKit = playerEntry.get(plugin.getGamemode().toString(), Document.class).getString("currentKit");
                if (currentKit != null) {
                    currentKits.put(uuid, currentKit);
                }

                if (!applyKitsOnce) {
                    List<String> kits = playerEntry.get(plugin.getGamemode().toString(), Document.class).getList("kits", String.class);

                    playerKits.put(
                            uuid,
                            kits != null ? kits : new ArrayList<>()
                    );
                }
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

    public boolean hasKit(UUID uuid, Kit kit) {
        return hasKit(uuid, kit.getName());
    }

    public boolean hasKit(UUID uuid, String name) {
        return playerKits.containsKey(uuid) && playerKits.get(uuid).contains(name);
    }

    public void addKit(GamePlayer gp, Kit kit) {
        if (!applyKitsOnce) {
            Player p = gp.bukkit();

            if (playerKits.containsKey(p.getUniqueId())) {
                playerKits.get(p.getUniqueId()).add(kit.getName());
            } else {
                playerKits.put(p.getUniqueId(), new ArrayList<>(Collections.singleton(kit.getName())));
            }

            savePlayerKits(p);
        }
    }

    public void removeKit(GamePlayer player, Kit kit) {
        if (!applyKitsOnce) {
            Player p = player.bukkit();

            playerKits.getOrDefault(p.getUniqueId(), Collections.emptyList()).remove(kit.getName());
            savePlayerKits(p);
        }
    }

    public void setKit(Kit kit, Player p) {
        if (clearInvOnKitSet && currentKits.containsKey(p.getUniqueId())) {
            Kit oldKit = getKit(currentKits.get(p.getUniqueId()));

            if (oldKit != null) {
                for (ItemStack item : oldKit.getKitItems().values()) {
                    p.getInventory().remove(item);
                }

                p.getInventory().setArmorContents(null);
            }
        }

        currentKits.put(p.getUniqueId(), kit.getName());
        system.getServer().getScheduler().runTaskAsynchronously(
                system,
                () -> PLAYER_KITS_COLLECTION.updateOne(
                        eq("uuid", p.getUniqueId().toString()),
                        set(plugin.getGamemode().toString()+".currentKit", kit.getName()),
                        new UpdateOptions().upsert(true)
                )
        );

        Map<Integer, ItemStack> items = calculateItems(kit, p);
        List<ItemStack> addLater = new ArrayList<>();

        for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
            if (p.getInventory().getItem(item.getKey()) != null && !p.getInventory().getItem(item.getKey()).getType().equals(Material.AIR)) {
                addLater.add(item.getValue());
            } else {
                p.getInventory().setItem(item.getKey(), item.getValue());
            }
        }

        p.getInventory().addItem(addLater.toArray(new ItemStack[0]));
    }

    public Kit getCurrentKit(Player p) {
        return getKit(currentKits.getOrDefault(p.getUniqueId(), null));
    }

    @Override
    public void openKitsInventory(Player p, Runnable onBackClick) {
        new KitsInventory(p, this, onBackClick);
    }

    @Override
    public ModifiedKit getModifiedKit(Player p, String name) {
        return getModifiedKit(name, customKits.getOrDefault(p.getUniqueId(), Collections.emptyList()));
    }

    @Override
    public boolean hasKitModified(Player p, String name) {
        return getModifiedKit(p, name) != null;
    }

    @Override
    public void modifyKit(Player player, Kit kit, Map<ItemStack, Integer> items) {
        Map<Integer, Integer> customItems = new HashMap<>();

        for (Map.Entry<Integer, ItemStack> kitItem : kit.getKitItems().entrySet()) {
            if (items.containsKey(kitItem.getValue())) {
                customItems.put(kitItem.getKey(), items.get(kitItem.getValue()));
            }
        }

        ModifiedKit customKit = new ModifiedKit(System.currentTimeMillis() / 1000, kit.getName(), customItems);

        if (customKits.containsKey(player.getUniqueId())) {
            ModifiedKit old = getModifiedKit(kit.getName(), customKits.get(player.getUniqueId()));
            if (old != null) {
                customKits.get(player.getUniqueId()).remove(old);
            }

            customKits.get(player.getUniqueId()).add(customKit);
        } else {
            customKits.put(player.getUniqueId(), new ArrayList<>(Collections.singleton(customKit)));
        }

        KIT_COLLECTION.updateOne(
                combine(
                        eq("uuid", player.getUniqueId().toString()),
                        eq("gamemode", plugin.getGamemode().name()),
                        eq("name", customKit.getName())
                ),
                combine(
                        set("lastUpdated", customKit.getLastUpdated()),
                        set("items", customKit.getCustomItems())
                ),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public Map<Integer, ItemStack> calculateItems(Kit kit, Player player) {
        if (customKits.containsKey(player.getUniqueId())) {
            ModifiedKit modifiedKit = getModifiedKit(kit.getName(), customKits.get(player.getUniqueId()));

            if (modifiedKit != null) {
                Map<Integer, ItemStack> items = new HashMap<>();

                for (Map.Entry<Integer, ItemStack> entry : kit.getKitItems().entrySet()) {
                    if (modifiedKit.calculateCustomItems().containsKey(entry.getKey())) {
                        items.put(modifiedKit.calculateCustomItems().get(entry.getKey()), entry.getValue());
                    }
                }

                return items;
            }
        }

        return kit.getKitItems();
    }

    private void savePlayerKits(Player p) {
        PLAYER_KITS_COLLECTION.updateOne(
                eq("uuid", p.getUniqueId().toString()),
                set(plugin.getGamemode().toString()+".kits", playerKits.get(p.getUniqueId())),
                new UpdateOptions().upsert(true)
        );
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
