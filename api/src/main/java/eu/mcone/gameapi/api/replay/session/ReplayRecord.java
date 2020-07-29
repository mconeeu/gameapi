package eu.mcone.gameapi.api.replay.session;

import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.utils.ReplayRecorder;
import org.bukkit.entity.Player;

import java.util.*;

public interface ReplayRecord {

    String getID();

    Gamemode getGamemode();

    List<Option> getOptions();

    ReplayRecorder getRecorder();

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
