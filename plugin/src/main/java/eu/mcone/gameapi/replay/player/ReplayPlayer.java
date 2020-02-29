package eu.mcone.gameapi.replay.player;

import eu.mcone.coresystem.api.bukkit.event.npc.NpcInteractEvent;
import eu.mcone.coresystem.api.bukkit.npc.NPC;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInteractInventory;
import eu.mcone.gameapi.replay.utils.Replay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator
public class ReplayPlayer implements eu.mcone.gameapi.api.replay.player.ReplayPlayer, Listener {

    @Getter
    private UUID uuid;
    @Getter
    private Data data;
    @Getter
    @Setter
    @BsonIgnore
    private transient ItemStack[] inventoryContent;
    @Getter
    @Setter
    @BsonIgnore
    private transient ReplayPlayer.Stats stats;
    @Getter
    @Setter
    @BsonIgnore
    private transient double health;

    @Getter
    private HashMap<String, List<PacketWrapper>> packets;

    @BsonIgnore
    @Getter
    private transient List<Player> viewInventory;

    @Getter
    @BsonIgnore
    private transient Replay replay;

    public ReplayPlayer(final Player player) {
        uuid = player.getUniqueId();
        data = new Data(player);
        packets = new HashMap<>();
        replay = new Replay(this);
        viewInventory = new ArrayList<>();
    }

    @BsonCreator
    public ReplayPlayer(@BsonProperty("uuid") final UUID uuid, @BsonProperty("data") final Data data, @BsonProperty("packets") final HashMap<String, List<PacketWrapper>> packets) {
        this.uuid = uuid;
        this.data = data;
        this.packets = packets;
        replay = new Replay(this);
        viewInventory = new ArrayList<>();
    }

    public void addPacket(final int tick, final PacketWrapper packet) {
        String sTick = String.valueOf(tick);
        System.out.println("Add packet " + sTick);
        if (packets.containsKey(sTick)) {
            packets.get(sTick).add(packet);
        } else {
            packets.put(sTick, new ArrayList<PacketWrapper>() {{
                add(packet);
            }});
        }
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
            if (inv.getName().equalsIgnoreCase(replay.getPlayer().getData().getDisplayName())) {
                if (viewInventory.contains(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getPlayer();
        if (inv.getName().equalsIgnoreCase(replay.getPlayer().getData().getDisplayName())) {
            if (viewInventory.contains(player)) {
                new ReplayPlayerInteractInventory(replay, player);
                viewInventory.remove(player);
            }
        }
    }

    @EventHandler
    public void on(NpcInteractEvent e) {
        NPC npc = e.getNpc();

        if (npc instanceof PlayerNpc) {
            if (npc.getData().getName().equalsIgnoreCase(replay.getPlayer().getUuid().toString())) {
                new ReplayPlayerInteractInventory(replay, e.getPlayer());
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @BsonDiscriminator
    public static class Data implements eu.mcone.gameapi.api.replay.player.ReplayPlayer.Data {
        private String displayName;
        private String name;
        private boolean reported;
        private SkinInfo skinInfo;
        @Setter
        private long joined;

        public Data(final Player player) {
            this.displayName = player.getDisplayName();
            this.name = player.getName();
            this.reported = false;

//            Property textures = ((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures").iterator().next();
//            this.skinInfo = new SkinInfo(name, textures.getValue(), textures.getSignature(), SkinInfo.SkinType.PLAYER);
        }

        @BsonCreator
        public Data(@BsonProperty("displayName") String displayName, @BsonProperty("name") String name,
                    @BsonProperty("reported") boolean reported, @BsonProperty("skinInfo") SkinInfo skinInfo, @BsonProperty("joined") long joined) {
            this.displayName = displayName;
            this.name = name;
            this.reported = reported;
            this.skinInfo = skinInfo;
            this.joined = joined;
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Stats implements eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats {
        @BsonIgnore
        private int kills;
        @BsonIgnore
        private int deaths;
        @BsonIgnore
        private int goals;
    }
}
