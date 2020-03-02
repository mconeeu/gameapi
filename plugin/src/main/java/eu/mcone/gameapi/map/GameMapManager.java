package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.map.MapRotationHandler;
import eu.mcone.gameapi.api.map.MapVotingHandler;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class GameMapManager implements MapManager {

    @Getter
    private final GameAPIPlugin system;
    @Getter
    private final LinkedList<GameAPIMap> maps;
    @Getter
    private final CoreJsonConfig<MapsConfig> config;

    @Getter
    private GameMapVotingHandler votingHandler;
    @Getter
    private GameMapRotationHandler rotationHandler;

    public GameMapManager(GameAPIPlugin system) throws IllegalStateException {
        this.system = system;
        this.maps = new LinkedList<>();
        this.config = new CoreJsonConfig<>(system, MapsConfig.class, "maps.json");

        system.sendConsoleMessage("§aLoading MapManager...");
        for (GameAPIMap map : config.parseConfig().getMaps()) {
            if (map.getWorld() != null) {
                system.sendConsoleMessage("§2Loading Map "+map.getName()+"...");
                this.maps.add(map);
            } else {
                throw new IllegalStateException("Map "+map.getName()+" could not be loaded in Mapmanager. World was not loaded by the CoreSystem!");
            }
        }
    }

    @Override
    public GameMapManager addMap(CoreWorld world, Material item) {
        MapsConfig config = this.config.parseConfig();
        config.addWorld(world, item);

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

}
