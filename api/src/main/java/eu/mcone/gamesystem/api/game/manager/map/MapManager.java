package eu.mcone.gamesystem.api.game.manager.map;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import org.bukkit.entity.Player;

import java.io.IOException;

public interface MapManager {

    MapItem getGainedMap();

    /**
     * Create a json file in which all maps that are to be available for voting can be entered.
     * @throws IOException Catches a possible IOException that could be thrown when creating the JSON file.
     */
    void createVotingSelection() throws IOException;

    /**
     * Inserts all maps in the JSON file into an ArrayList.
     * @throws IOException Catches a possible IOException that might occur while reading the JSON file.
     */
    void addMaps() throws IOException;

    /**
     * Returns the map with the specified map name
     * @param map Map name after being searched
     * @return returns a MapItem object.
     */
    MapItem getMap(final String map);

    /**
     * Ends the voting and returns the won Map as MapItem
     * @return returns a MapItem object that won the voting.
     */
    MapItem closeVoting();

    /**
     * Creates the Map Voting Inventory.
     * @param player Player for which the inventory is to be created.
     */
    void createMapInventory(final Player player, CoreInventory returnInventory);

}
