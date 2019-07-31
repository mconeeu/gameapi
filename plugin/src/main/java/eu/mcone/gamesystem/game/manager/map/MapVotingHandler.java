package eu.mcone.gamesystem.game.manager.map;

import eu.mcone.gamesystem.api.game.event.GameMapChangeEvent;
import eu.mcone.gamesystem.api.game.manager.map.GameMap;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.api.game.manager.map.IMapVotingHandler;
import eu.mcone.gamesystem.game.inventory.MapInventory;
import eu.mcone.networkmanager.core.api.console.ConsoleColor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log
public class MapVotingHandler implements IMapVotingHandler {

    @Getter
    private MapManager mapManager;

    @Getter
    private Map<String, Integer> mapPopularity;
    @Getter
    private Map<Player, String> mapVoting;
    @Getter
    private GameMap gainedMap;

    public MapVotingHandler(MapManager mapManager) {
        this.mapManager = mapManager;

        mapPopularity = new HashMap<>();
        mapVoting = new HashMap<>();
    }

    public GameMap closeMapVoting() {
        if (mapManager.getOptions().contains(IMapManager.Options.MAP_INVENTORY)) {
            if (mapVoting.size() != 0) {
                for (int i = 0; i <= mapVoting.size(); i++) {
                    for (Map.Entry<String, Integer> entry : this.mapPopularity.entrySet()) {
                        if (entry.getValue() < i || entry.getValue() == i) {
                            gainedMap = mapManager.getGameMap(entry.getKey());
                            log.info(ConsoleColor.GREEN + "Return the world `" + entry.getKey() + "`");
                            return gainedMap;
                        }
                    }
                }
            } else {
                gainedMap = mapManager.getGameMaps().get(new Random().nextInt(mapManager.getGameMaps().size()));
                log.info(ConsoleColor.GREEN + "Return the random map `" + gainedMap.getWorld() + "`");
                return gainedMap;
            }

            Bukkit.getServer().getPluginManager().callEvent(
                    new GameMapChangeEvent(
                            gainedMap
                    )
            );
        } else {
            mapManager.getCoreInstance().sendConsoleMessage("§cSorry can not return a game map because the Map Rotation option is activated");
        }

        return null;
    }

    public void createMapInventory(final Player player) {
        new MapInventory(player, this);
    }
}
