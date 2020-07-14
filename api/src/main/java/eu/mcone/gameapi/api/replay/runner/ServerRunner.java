package eu.mcone.gameapi.api.replay.runner;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface ServerRunner {

    Collection<Player> getWatchers();
}
