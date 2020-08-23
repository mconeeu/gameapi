package eu.mcone.gameapi.api.replay;

import eu.mcone.gameapi.api.game.GameHistory;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.utils.ReplayRecorder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ReplayRecord {

    String getID();

    String getGameID();

    ReplayRecorder getRecorder();

    GameHistory getGameHistory();

    void recordSession();

    void save();

    void addPlayer(final Player player);

    void removePlayer(final Player player);

    ReplayPlayer getReplayPlayer(final UUID uuid);

    ReplayPlayer getReplayPlayer(final Player player);

    Map<String, ReplayPlayer> getPlayers();

    Collection<ReplayPlayer> getPlayersAsObject();

    boolean existsReplayPlayer(final UUID uuid);

    boolean existsReplayPlayer(final Player player);
}
