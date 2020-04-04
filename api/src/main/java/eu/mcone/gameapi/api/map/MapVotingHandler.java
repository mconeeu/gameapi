package eu.mcone.gameapi.api.map;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface MapVotingHandler {

    void openVotingInventory(Player p, CoreInventory coreInventory);

    Map<GameAPIMap, List<Player>> getPopularityMap();

    void vote(Player player, GameAPIMap map, CoreInventory coreInventory);

    GameAPIMap calculateResult();

}
