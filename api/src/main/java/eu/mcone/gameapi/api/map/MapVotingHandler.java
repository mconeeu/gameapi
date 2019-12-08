package eu.mcone.gameapi.api.map;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface MapVotingHandler {

    void openVotingInventory(Player p);

    Map<GameAPIMap, List<Player>> getPopularityMap();

    void vote(Player player, GameAPIMap map);

    GameAPIMap calculateResult();

}
