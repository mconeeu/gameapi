package eu.mcone.gameapi.kit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.KitSortListener;
import lombok.Getter;
import org.bson.Document;
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
    private final boolean clearInvOnKitSet;
    private final GamePlugin plugin;

    @Getter
    private final List<Kit> kits;
    private final Map<UUID, List<String>> playerKits;
    private final Map<UUID, List<ModifiedKit>> customKits;

    public GameKitManager(GameAPIPlugin system, GamePlugin plugin, Option... options) {
        system.registerEvents(new KitSortListener(this));

        this.clearInvOnKitSet = Arrays.asList(options).contains(Option.CLEAR_INVENTORY_ON_KIT_SET);
        this.plugin = plugin;

        this.kits = new ArrayList<>();
        this.playerKits = new HashMap<>();
        this.customKits = new HashMap<>();

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
            List<String> kits = playerEntry.get("items", Document.class).getList(plugin.getGamemode().toString(), String.class);

            playerKits.put(
                    UUID.fromString(playerEntry.getString("uuid")),
                    kits != null ? kits : new ArrayList<>()
            );
        }
    }

    public void registerKits(Kit... kit) {
        kits.addAll(Arrays.asList(kit));
    }

    public void removeKit(String name) {
        kits.removeIf(kit -> kit.getName().equals(name));
    }

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
        Player p = gp.bukkit();

        if (playerKits.containsKey(p.getUniqueId())) {
            playerKits.get(p.getUniqueId()).add(kit.getName());
        } else {
            playerKits.put(p.getUniqueId(), new ArrayList<>(Collections.singleton(kit.getName())));
        }

        savePlayerKits(p);
    }

    public void removeKit(GamePlayer player, Kit kit) {
        Player p = player.bukkit();

        playerKits.getOrDefault(p.getUniqueId(), Collections.emptyList()).remove(kit.getName());
        savePlayerKits(p);
    }

    public void setKit(Kit kit, Player p) {
        if (clearInvOnKitSet) {
            p.getInventory().clear();
        }

        Map<Integer, ItemStack> items = calculateItems(kit, p);
        List<ItemStack> addLater = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
            if (p.getInventory().getItem(item.getKey()) != null) {
                p.getInventory().setItem(item.getKey(), item.getValue());
            } else {
                addLater.add(item.getValue());
            }
        }

        p.getInventory().addItem(addLater.toArray(new ItemStack[0]));
    }

    public ModifiedKit getModifiedKit(Player p, String name) {
        return getModifiedKit(name, customKits.getOrDefault(p.getUniqueId(), Collections.emptyList()));
    }

    public boolean hasKitModified(Player p, String name) {
        return getModifiedKit(p, name) != null;
    }

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

    public Map<Integer, ItemStack> calculateItems(Kit kit, Player player) {
        if (customKits.containsKey(player.getUniqueId())) {
            ModifiedKit modifiedKit = getModifiedKit(kit.getName(), customKits.get(player.getUniqueId()));

            if (modifiedKit != null) {
                Map<Integer, ItemStack> items = new HashMap<>();

                for (Map.Entry<Integer, ItemStack> entry : kit.getKitItems().entrySet()) {
                    if (modifiedKit.getCustomItems().containsKey(entry.getKey())) {
                        items.put(modifiedKit.getCustomItems().get(entry.getKey()), entry.getValue());
                    }
                }

                return items;
            }
        }

        return kit.getKitItems();
    }

    private void savePlayerKits(Player p) {
        PLAYER_KITS_COLLECTION.updateOne(
                eq("uuid", p.getUniqueId()),
                combine(
                        set("items."+plugin.getGamemode().toString(), playerKits.get(p.getUniqueId()))
                ),
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
