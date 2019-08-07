package eu.mcone.gamesystem.api.lobby.manager;

import eu.mcone.gamesystem.api.enums.Item;
import org.bukkit.entity.Player;

public interface TrailManager {

    void shutdown();

    void setTrail(Player p, Item trail);

    void removeTrail(Player p);

    void unregisterPlayer(Player p);
}
