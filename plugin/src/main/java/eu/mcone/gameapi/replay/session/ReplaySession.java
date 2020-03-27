package eu.mcone.gameapi.replay.session;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplaySessionEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplaySessionEvent;
import eu.mcone.gameapi.api.replay.exception.ReplayPlayerAlreadyExistsException;
import eu.mcone.gameapi.api.replay.session.ReplaySessionManager;
import eu.mcone.gameapi.replay.chunk.ReplayChunkHandler;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.runner.ReplayRunnerManager;
import eu.mcone.gameapi.replay.utils.ReplayRecorder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class ReplaySession implements eu.mcone.gameapi.api.replay.session.ReplaySession {

    @Getter
    private String ID;
    @Getter
    private ServerInfo info;

    //Contains Broadcast messages !
    @Getter
    private HashMap<String, List<PacketWrapper>> messages;
    @Getter
    private HashMap<String, ReplayPlayer> replayPlayers;

    private transient ReplayChunkHandler chunkHandler;
    private transient ReplayRunnerManager runnerManager;
    private transient ReplayRecorder replayRecorder;

    public ReplaySession(final ReplaySessionManager replaySessionManager) {
        this.ID = replaySessionManager.getSessionID();

        info = new ServerInfo();
        messages = new HashMap<>();
        replayPlayers = new HashMap<>();

        replayRecorder = new ReplayRecorder(this);
        chunkHandler = new ReplayChunkHandler(this);
        runnerManager = new ReplayRunnerManager(this);
    }

    @BsonCreator
    public ReplaySession(@BsonProperty("ID") String ID, @BsonProperty("info") ServerInfo info, @BsonProperty("messages") HashMap<String, List<PacketWrapper>> messages, @BsonProperty("replayPlayers") HashMap<String, ReplayPlayer> replayPlayers) {
        this.ID = ID;
        this.info = info;
        this.messages = messages;
        this.replayPlayers = replayPlayers;

        chunkHandler = new ReplayChunkHandler(this);
        runnerManager = new ReplayRunnerManager(this);
    }

    public void recordSession() {
        info.setWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getGameWorld());

        boolean succeed = true;
        if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(getInfo().getWorld())) {
            succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(getInfo().getWorld()));
        }

        if (succeed) {
            replayRecorder.record();

            //Adds the world entity spawn packet
            for (ReplayPlayer player : replayPlayers.values()) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinReplaySessionEvent(Bukkit.getPlayer(player.getUuid())));
            }
        } else {
            throw new IllegalStateException("[REPLAY] Could not upload World " + info.getWorld() + " to database!");
        }
    }

    public void saveSession() {
        replayRecorder.stop();
        GamePlugin.getGamePlugin().getSessionManager().saveSession(this);
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

    @BsonIgnore
    public Collection<ReplayPlayer> getPlayersAsObject() {
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
    public static class ServerInfo implements eu.mcone.gameapi.api.replay.session.ReplaySession.ServerInfo {
        private String uuid;
        private long started;
        private long stopped;
        private int teams;
        private String winnerTeam = "Nicht verfügbar";
        private String world;
        private int lastTick;
    }

    public void openInformationInventory(Player player) {
        new ReplayInformationInventory(player, this);
    }
}
