package eu.mcone.gameapi.api.backpack.handler;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import org.bukkit.entity.Player;

public interface OutfitHandler {

    void setOutfit(Player p, BackpackItem item);

    void setOutfit(Player p, DefaultItem item);
}
