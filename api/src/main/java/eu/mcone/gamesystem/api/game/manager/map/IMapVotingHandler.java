package eu.mcone.gamesystem.api.game.manager.map;

import org.bukkit.entity.Player;

public interface IMapVotingHandler {

    /**
     * Ends the voting and returns the won Map as MapItem
     *
     * @return returns a MapItem object that won the voting.
     */
    GameMap closeMapVoting();

    /**
     * Creates the Map Voting Inventory.
     *
     * @param player Player for which the inventory is to be created.
     */
    void createMapInventory(final Player player);
}
