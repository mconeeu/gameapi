package eu.mcone.gameapi.replay;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.game.GameHistory;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplayEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplayEvent;
import eu.mcone.gameapi.api.replay.exception.ReplayPlayerAlreadyExistsException;
import eu.mcone.gameapi.api.utils.IDUtils;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.utils.ReplayRecorder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class ReplayRecord implements eu.mcone.gameapi.api.replay.ReplayRecord {

    @Getter
    private String ID;
    @Getter
    private String gameID;

    private Map<String, eu.mcone.gameapi.api.replay.player.ReplayPlayer> players;
    private ReplayRecorder recorder;

    public ReplayRecord(String ID, String gameID) {
        this.ID = ID;
        this.gameID = gameID;

        players = new HashMap<>();
        recorder = new ReplayRecorder(this, GamePlugin.getGamePlugin().getReplayManager().getCodecRegistry());
    }

    public ReplayRecord(String gameID) {
        this.ID = IDUtils.generateID();

        if (GamePlugin.getGamePlugin().getReplayManager().existsReplay(ID)) {
            ID = IDUtils.generateID();
        }

        this.gameID = gameID;

        players = new HashMap<>();
        recorder = new ReplayRecorder(this, GamePlugin.getGamePlugin().getReplayManager().getCodecRegistry());
    }

    public GameHistory getGameHistory() {
        return GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory();
    }

    public void recordSession() {
        if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(recorder.getWorld())) {
            CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(recorder.getWorld()), (uploaded) -> {
                if (uploaded) {
                    recorder.record();

                    //Adds the world entity spawn packet
                    for (eu.mcone.gameapi.api.replay.player.ReplayPlayer player : players.values()) {
                        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinReplayEvent(bukkitPlayer));
                    }
                } else {
                    throw new IllegalStateException("[REPLAY] Could not upload World " + recorder.getWorld() + " to database!");
                }
            });
        }
    }

    public void save() {
        recorder.stop();
        GamePlugin.getGamePlugin().getReplayManager().saveReplay(this);
    }

    public void addPlayer(final Player player) {
        try {
            if (!players.containsKey(player.getUniqueId().toString())) {
                players.put(player.getUniqueId().toString(), new ReplayPlayer(player));
            } else {
                throw new ReplayPlayerAlreadyExistsException();
            }
        } catch (ReplayPlayerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(final Player player) {
        try {
            if (players.containsKey(player.getUniqueId().toString())) {
                players.get(player.getUniqueId().toString()).setLeaved(System.currentTimeMillis() / 1000);
                Bukkit.getServer().getPluginManager().callEvent(new PlayerQuitReplayEvent(player));
            } else {
                throw new NullPointerException("ReplayPlayer " + player.getName() + " doesnt exists!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid) {
        return players.getOrDefault(uuid.toString(), null);
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player) {
        return getReplayPlayer(player.getUniqueId());
    }

    public Collection<eu.mcone.gameapi.api.replay.player.ReplayPlayer> getPlayersAsObject() {
        return new ArrayList<>(players.values());
    }

    public boolean existsReplayPlayer(final UUID uuid) {
        return players.containsKey(uuid.toString());
    }

    public boolean existsReplayPlayer(final Player player) {
        return existsReplayPlayer(player.getUniqueId());
    }
}
