package eu.mcone.gameapi.api.map;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface MapManager {

    MapManager addMap(CoreWorld world, Material item);

    GameAPIMap getMap(String name);

    MapVotingHandler getMapVotingHandler() throws IllegalStateException;

    MapRotationHandler getMapRotationHandler() throws IllegalStateException;

}
