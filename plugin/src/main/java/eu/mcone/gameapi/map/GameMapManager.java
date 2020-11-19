package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.map.MapRotationHandler;
import eu.mcone.gameapi.api.map.MapVotingHandler;
import eu.mcone.gameapi.command.MapCMD;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameMapManager implements MapManager {

    @Getter
    private final GamePlugin system;
    @Getter
    private final LinkedList<GameAPIMap> maps;
    @Getter
    private final CoreJsonConfig<MapsConfig> config;

    private GameMapVotingHandler votingHandler;
    private GameMapRotationHandler rotationHandler;

    public GameMapManager(GamePlugin system) throws IllegalStateException {
        this.system = system;
        this.maps = new LinkedList<>();
        this.config = new CoreJsonConfig<>(system, MapsConfig.class, "maps.json");

        system.sendConsoleMessage("§aLoading MapManager...");
        system.registerCommands(new MapCMD(this));

        for (GameAPIMap map : config.parseConfig().getMaps()) {
            if (map.getWorld() != null) {
                system.sendConsoleMessage("§2Loading Map " + map.getName() + "...");
                this.maps.add(map);
            } else {
                throw new IllegalStateException("Map " + map.getName() + " could not be loaded in Mapmanager. World was not loaded by the CoreSystem!");
            }
        }
    }

    @Override
    public GameMapManager addMap(CoreWorld world, Material item) {
       return addMap(world, Collections.emptyList(), item);
    }

    @Override
    public GameMapManager addMap(CoreWorld world, List<String> lore, Material item) {
        MapsConfig config = this.config.parseConfig();
        config.addWorld(world, lore, item);

        this.config.updateConfig(config);
        return this;
    }

    @Override
    public GameAPIMap getMap(String name) {
        for (GameAPIMap map : maps) {
            if (name.equals(map.getName())) {
                return map;
            }
        }
        return null;
    }

    @Override
    public MapVotingHandler getMapVotingHandler() throws IllegalStateException {
        return votingHandler != null ? votingHandler : (votingHandler = new GameMapVotingHandler(this));
    }

    @Override
    public MapRotationHandler getMapRotationHandler() throws IllegalStateException {
        return rotationHandler != null ? rotationHandler : (rotationHandler = new GameMapRotationHandler(this));
    }

    public boolean isVotingHandlerLoaded() {
        return votingHandler != null;
    }

    public boolean isRotationHandlerLoaded() {
        return rotationHandler != null;
    }

}
