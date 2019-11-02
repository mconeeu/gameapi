package eu.mcone.gamesystem.api.game.manager.map;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;

public interface MapRotationHandler {

    /**
     * Returns the currently selected gameMap
     *
     * @return GameMap
     */
    GameMap getCurrentGameMap();

    /**
     * Returns the currently selected core world
     *
     * @return CoreWorld
     */
    CoreWorld getCurrentCoreWorld();

    /**
     * Starts the map rotation with the specified rotation interval
     */
    void startRotation();

    /**
     * Stops the currently running map rotation
     */
    void stopRotation();
}
