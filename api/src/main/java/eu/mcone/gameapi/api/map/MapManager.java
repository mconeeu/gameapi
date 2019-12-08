package eu.mcone.gameapi.api.map;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import org.bukkit.inventory.ItemStack;

public interface MapManager {

    void addMap(CoreWorld world, ItemStack item);

    GameAPIMap getMap(String name);

    MapVotingHandler getMapVotingHandler() throws IllegalStateException;

    MapRotationHandler getMapRotationHandler() throws IllegalStateException;

}
