package eu.mcone.gameapi.api.map;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import org.bukkit.Material;

import java.util.List;

public interface MapManager {

    MapManager addMap(CoreWorld world, List<String> lore, Material item);

    MapManager addMap(CoreWorld world, Material item);

    GameAPIMap getMap(String name);

    MapVotingHandler getMapVotingHandler() throws IllegalStateException;

    MapRotationHandler getMapRotationHandler() throws IllegalStateException;

}
