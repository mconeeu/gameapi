package eu.mcone.gameapi.replay.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@BsonDiscriminator
public class ReplayPlayer implements eu.mcone.gameapi.api.replay.player.ReplayPlayer, Listener {

    @Getter
    private UUID uuid;
    @Getter
    private Data data;
    @Setter
    @BsonIgnore
    private Map<Integer, SerializableItemStack> inventoryItems;
    @Getter
    @Setter
    @BsonIgnore
    private eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats stats;
    @Getter
    @Setter
    @BsonIgnore
    private int health;
    @Getter
    @Setter
    @BsonIgnore
    private int food;
    @Getter
    @BsonIgnore
    private Map<Player, CoreInventory> inventoryViewers;
    @Getter
    @Setter
    @BsonIgnore
    private PlayerNpc npc;

    public ReplayPlayer(final Player player) {
        uuid = player.getUniqueId();
        data = new Data(player);
        inventoryItems = new HashMap<>();
        stats = new Stats(0, 0, 0);
        health = 10;
        food = 20;
        inventoryViewers = new HashMap<>();
    }

    @BsonCreator
    public ReplayPlayer(@BsonProperty("uuid") final UUID uuid, @BsonProperty("data") final Data data) {
        this.uuid = uuid;
        this.data = data;
        inventoryItems = new HashMap<>();
        stats = new Stats(0, 0, 0);
        health = 10;
        inventoryViewers = new HashMap<>();
    }

    public void setNpc(PlayerNpc npc) {
        this.npc = npc;
    }

    public void setInventoryItem(int slot, SerializableItemStack itemStack) {
        inventoryItems.put(slot, itemStack);
    }

    public void openInventory(Player player) {
        inventoryViewers.put(player, new ReplayPlayerInventory(player, this, inventoryItems));
    }

    @Getter
    @BsonDiscriminator
    public static class Data implements eu.mcone.gameapi.api.replay.player.ReplayPlayer.Data {
        private String displayName;
        private String name;
        @Setter
        private String sessionID;
        @Setter
        private boolean reported;
        @Setter
        private long joined;
        @Setter
        private CoreLocation spawnLocation;

        public Data(final Player player) {
            CorePlayer cp = CoreSystem.getInstance().getCorePlayer(player.getUniqueId());
            this.displayName = (cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + player.getName();
            this.name = player.getName();
            this.reported = false;
        }

        @BsonCreator
        public Data(@BsonProperty("displayName") final String displayName, @BsonProperty("name") final String name, @BsonProperty("sessionID") final String sessionID,
                    @BsonProperty("reported") final boolean reported, @BsonProperty("joined") final long joined, @BsonProperty("spawnLocation") CoreLocation spawnLocation) {
            this.displayName = displayName;
            this.name = name;
            this.sessionID = sessionID;
            this.reported = reported;
            this.joined = joined;
            this.spawnLocation = spawnLocation;
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stats implements eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats {
        private int kills = 0;
        private int deaths = 0;
        private int goals = 0;
    }
}
