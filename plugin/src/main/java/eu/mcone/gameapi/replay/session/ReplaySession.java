package eu.mcone.gameapi.replay.session;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplaySessionEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplaySessionEvent;
import eu.mcone.gameapi.api.replay.exception.ReplayPlayerAlreadyExistsException;
import eu.mcone.gameapi.api.replay.session.ReplaySessionManager;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import eu.mcone.gameapi.replay.inventory.ReplaySpectatorInventory;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.utils.ReplayRecorder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@BsonDiscriminator
public class ReplaySession implements eu.mcone.gameapi.api.replay.session.ReplaySession {

    @Getter
    private String ID;
    @Getter
    private ServerInfo info;

    //Contains Broadcast messages !
    @Getter
    private HashMap<String, List<PacketWrapper>> serverPackets;
    @Getter
    private HashMap<String, List<PacketWrapper>> worldPackets;
    @Getter
    private HashMap<String, ReplayPlayer> replayPlayers;

    @Getter
    private transient ReplayRecorder replayRecorder;

    public ReplaySession(final ReplaySessionManager replaySessionManager) {
        this.ID = replaySessionManager.getSessionID();

        info = new ServerInfo();

        serverPackets = new HashMap<>();
        worldPackets = new HashMap<>();
        replayPlayers = new HashMap<>();

        replayRecorder = new ReplayRecorder(this);
    }

    @BsonCreator
    public ReplaySession(@BsonProperty("sessionID") String sessionID, @BsonProperty("info") ServerInfo info, @BsonProperty("serverPackets") HashMap<String, List<PacketWrapper>> serverPackets,
                         @BsonProperty("worldPackets") HashMap<String, List<PacketWrapper>> worldPackets,
                         @BsonProperty("replayPlayers") HashMap<String, ReplayPlayer> replayPlayers) {
        this.ID = sessionID;
        this.info = info;
        this.serverPackets = serverPackets;
        this.worldPackets = worldPackets;
        this.replayPlayers = replayPlayers;
    }

    public void recordSession() {
        info.setWorld(GamePlugin.getPlugin().getGameConfig().parseConfig().getGameWorld());
        replayRecorder.record();

        //Adds the world entity spawn packet
        for (ReplayPlayer player : replayPlayers.values()) {
            System.out.println("SPAWN");
            Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinReplaySessionEvent(Bukkit.getPlayer(player.getUuid())));
        }
    }

    public void saveSession() {
        replayRecorder.stop();
        GamePlugin.getPlugin().getSessionManager().saveSession(this);
    }

    public void addPlayer(final Player player) {
        try {
            if (!replayPlayers.containsKey(player.getUniqueId().toString())) {
                replayPlayers.put(player.getUniqueId().toString(), new ReplayPlayer(player));
            } else {
                throw new ReplayPlayerAlreadyExistsException();
            }
        } catch (ReplayPlayerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(final Player player) {
        try {
            if (replayPlayers.containsKey(player.getUniqueId().toString())) {
                replayPlayers.remove(player.getUniqueId().toString());
                Bukkit.getServer().getPluginManager().callEvent(new PlayerQuitReplaySessionEvent(player));
            } else {
                throw new NullPointerException("ReplayPlayer " + player.getName() + " doesnt exists!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid) {
        return replayPlayers.getOrDefault(uuid.toString(), null);
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player) {
        return getReplayPlayer(player.getUniqueId());
    }

    @BsonIgnore
    public Collection<eu.mcone.gameapi.api.replay.player.ReplayPlayer> getPlayers() {
        return new ArrayList<>(replayPlayers.values());
    }

    public boolean existsReplayPlayer(final UUID uuid) {
        return replayPlayers.containsKey(uuid.toString());
    }

    public boolean existsReplayPlayer(final Player player) {
        return existsReplayPlayer(player.getUniqueId());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @BsonDiscriminator
    public static class ServerInfo implements eu.mcone.gameapi.api.replay.session.ReplaySession.ServerInfo {
        private String uuid;
        private long started;
        private long stopped;
        private int teams;
        private String winnerTeam;
        private String world;

        //In minutes
        private double length;

        @BsonCreator
        public ServerInfo(@BsonProperty("uuid") final String uuid, @BsonProperty("started") final long started, @BsonProperty("stopped") final long stopped,
                          @BsonProperty("teams") final int teams, @BsonProperty("winnerTeam") final String winnerTeam, @BsonProperty("length") final double length, @BsonProperty("world") final String world) {
            this.uuid = uuid;
            this.started = started;
            this.stopped = stopped;
            this.teams = teams;
            this.winnerTeam = winnerTeam;
            this.length = length;
            this.world = world;
        }
    }

    public void openInformationInventory(Player player) {
        new ReplayInformationInventory(player, this);
    }

    public void openSpectatorInventory(Player player) {
        new ReplaySpectatorInventory(this, player);
    }
}
