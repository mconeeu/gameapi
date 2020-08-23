package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface ServerRunner {

    ReplayContainer getContainer();

    boolean isSync();

    boolean isForward();

    boolean isPlaying();

    ReplaySpeed getSpeed();

    Collection<Player> getViewers();
}
