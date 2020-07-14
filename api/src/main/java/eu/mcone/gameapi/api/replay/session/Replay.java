package eu.mcone.gameapi.api.replay.session;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface Replay {

    String getID();

    long getStarted();

    long getStopped();

    String getWinnerTeam();

    String getWorld();

    Gamemode getGamemode();

    int getLastTick();

    eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid);

    eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player);

    Collection<ReplayPlayer> getPlayers();

    Collection<ReplayPlayer> getPlayersAsObject();

    boolean existsReplayPlayer(final UUID uuid);

    boolean existsReplayPlayer(final Player player);

    void openInformationInventory(Player player);

}
