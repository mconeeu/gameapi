package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.gameapi.api.event.map.MapVotedEvent;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapVotingHandler;
import eu.mcone.gameapi.listener.map.MapVoteListener;
import eu.mcone.gameapi.inventory.MapInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class GameMapVotingHandler implements MapVotingHandler {

    @Getter
    private final GameMapManager mapManager;

    private final List<Player> openedInventories;
    private final Map<Player, String> votes;
    private final Map<String, Integer> popularity;

    GameMapVotingHandler(GameMapManager mapManager) throws IllegalStateException {
        this.mapManager = mapManager;

        mapManager.getSystem().sendConsoleMessage("§aLoading Map VotingHandler...");
        mapManager.getSystem().registerEvents(new MapVoteListener(this));

        if (mapManager.getMaps().size() <= 0) {
            throw new IllegalStateException("MapManager has to few Maps registered!");
        }

        this.openedInventories = new ArrayList<>();
        this.votes = new HashMap<>();
        this.popularity = new HashMap<>();
    }

    @Override
    public void openVotingInventory(Player p, CoreInventory coreInventory) {
        openedInventories.add(p);
        new MapInventory(p, this, coreInventory);
    }

    public void closedInventory(Player p) {
        openedInventories.remove(p);
    }

    @Override
    public Map<GameAPIMap, List<Player>> getPopularityMap() {
        Map<GameAPIMap, List<Player>> result = new HashMap<>();
        for (GameAPIMap map : mapManager.getMaps()) {
            List<Player> votes = new ArrayList<>();

            for (Map.Entry<Player, String> vote : this.votes.entrySet()) {
                if (vote.getValue().equals(map.getName())) {
                    votes.add(vote.getKey());
                }
            }

            result.put(map, votes);
        }

        return result;
    }

    @Override
    public void vote(Player player, GameAPIMap map, CoreInventory coreInventory) {
        if (votes.containsKey(player)) {
            popularity.put(votes.get(player), popularity.get(votes.get(player))-1);
        }

        votes.put(player, map.getName());
        popularity.put(map.getName(), popularity.containsKey(map.getName()) ? popularity.get(map.getName())+1 : 1);

        for (Player p : openedInventories) {
            openVotingInventory(p, coreInventory);
        }
    }

    @Override
    public GameAPIMap calculateResult() {
        GameAPIMap votedMap;

        if (popularity.size() > 0) {
            Map.Entry<String, Integer> votedEntry = null;

            for (Map.Entry<String, Integer> vote : popularity.entrySet()) {
                if (votedEntry == null || vote.getValue() > votedEntry.getValue()) {
                    votedEntry = vote;
                }
            }

            votedMap = mapManager.getMap(votedEntry.getKey());
        } else {
            votedMap = mapManager.getMaps().get(new Random().nextInt(mapManager.getMaps().size()-1));
        }

        Bukkit.getPluginManager().callEvent(new MapVotedEvent(votedMap));
        return votedMap;
    }

}
