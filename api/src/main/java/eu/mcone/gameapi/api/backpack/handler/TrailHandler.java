package eu.mcone.gameapi.api.backpack.handler;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import org.bukkit.entity.Player;

public interface TrailHandler {
    void setTrail(Player p, BackpackItem trail);

    void removeTrail(Player p);

    void stop();
}
