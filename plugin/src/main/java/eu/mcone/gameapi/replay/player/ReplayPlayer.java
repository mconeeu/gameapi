package eu.mcone.gameapi.replay.player;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInventory;
import eu.mcone.gameapi.replay.npc.NpcUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.Serializable;
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
    private transient Map<Integer, SerializableItemStack> inventoryItems;
    @Getter
    @Setter
    private transient eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats stats;
    @Getter
    @Setter
    private transient double health;
    @Getter
    private transient Map<Player, CoreInventory> inventoryViewers;
    @Getter
    private transient PlayerNpc npc;

    public ReplayPlayer(final Player player) {
        uuid = player.getUniqueId();
        data = new Data(player);
        inventoryItems = new HashMap<>();
        stats = new Stats(0, 0, 0);
        health = 10.0;
        inventoryViewers = new HashMap<>();
    }

    @BsonCreator
    public ReplayPlayer(@BsonProperty("uuid") final UUID uuid, @BsonProperty("data") final Data data) {
        this.uuid = uuid;
        this.data = data;
        inventoryItems = new HashMap<>();
        stats = new Stats(0, 0, 0);
        health = 10.0;
        inventoryViewers = new HashMap<>();
        npc = NpcUtils.constructNpcForPlayer(data.getSessionID(), this);
    }

    public void openInventory(Player player) {
        inventoryViewers.put(player, new ReplayPlayerInventory(player, this, inventoryItems));
    }

    @Getter
    @BsonDiscriminator
    public static class Data implements Serializable, eu.mcone.gameapi.api.replay.player.ReplayPlayer.Data {
        private String displayName;
        private String name;
        @Setter
        private String sessionID;
        @Setter
        private boolean reported;
        @Setter
        private long joined;
        @Setter
        private String world;

        public Data(final Player player) {
            this.displayName = player.getDisplayName();
            this.name = player.getName();
            this.reported = false;
        }

        @BsonCreator
        public Data(@BsonProperty("displayName") final String displayName, @BsonProperty("name") final String name, @BsonProperty("sessionID") String sessionID,
                    @BsonProperty("reported") final boolean reported, @BsonProperty("joined") final long joined, @BsonProperty("world") final String world) {
            this.displayName = displayName;
            this.name = name;
            this.sessionID = sessionID;
            this.reported = reported;
            this.joined = joined;
            this.world = world;
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stats implements Serializable, eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats {
        private transient int kills = 0;
        private transient int deaths = 0;
        private transient int goals = 0;
    }
}
