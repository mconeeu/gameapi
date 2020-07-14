package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public interface PlayerRunner {

    ReplayContainer getContainer();

    Collection<Player> getWatchers();

    void setBreaking(boolean breaking);

    ReplayPlayer getPlayer();

    ReplaySpeed getReplaySpeed();

    void setReplaySpeed(ReplaySpeed speed);

    void skip(int ticks);
}
