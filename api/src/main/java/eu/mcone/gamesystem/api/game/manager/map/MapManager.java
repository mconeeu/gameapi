/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.manager.map;

public interface MapManager {

    /**
     * Creates an MapRotationHandler for the mapManager
     *
     * @param rotationInterval rotation interval in seconds
     * @return IMapRotationHandler
     */
    MapRotationHandler createMapRotationHandler(int rotationInterval);

    /**
     * Creates an MapVotingHandler for the mapManager
     *
     * @return IMapVotingHandler
     */
    MapVotingHandler createMapVotingHandler();

    /**
     * Returns the created MapRotationHandler
     *
     * @return IMapRotationHandler
     * @throws eu.mcone.gamesystem.api.ecxeptions.GameSystemException if the object is null
     */
    MapRotationHandler getMapRotationHandler();

    /**
     * Returns the created MapVotingHandler
     *
     * @return IMapVotingHandler
     * @throws eu.mcone.gamesystem.api.ecxeptions.GameSystemException if the object is null
     */
    MapVotingHandler getMapVotingHandler();

    /**
     * Returns a GameMap object for the specified world name
     *
     * @param world Name of the world
     * @return GameMap
     */
    GameMap getGameMap(final String world);

    /**
     * Create a json file in which all maps that are to be available for voting can be entered.
     */
    boolean loadGameWorlds();

    enum Options {
        MAP_INVENTORY(),
        MAP_ROTATION()
    }
}
