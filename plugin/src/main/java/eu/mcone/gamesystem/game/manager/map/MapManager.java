/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.map;

import com.google.gson.reflect.TypeToken;
import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.manager.map.*;
import lombok.Getter;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapManager implements IMapManager {

    @Getter
    private final CorePlugin coreInstance;

    @Getter
    private List<Options> options;
    @Getter
    private List<GameMap> gameMaps;

    private IMapRotationHandler mapRotationHandler;
    private IMapVotingHandler mapVotingHandler;

    private File dir;
    private File file;

    public MapManager(final CorePlugin coreInstance, final Options... options) {
        this.coreInstance = coreInstance;

        this.options = new ArrayList<>(Arrays.asList(options));
        gameMaps = new ArrayList<>();

        dir = new File("./plugins/" + GameTemplate.getInstance().getPluginName());
        file = new File(dir, "/worlds.json");

        if (loadGameWorlds()) {
            GameSystemAPI.getInstance().sendConsoleMessage("§aEs wurden alle Gamemaps geladen!");
        } else {
            GameSystemAPI.getInstance().sendConsoleMessage("§cEs konnten nicht alle Maps geladen werden!");
        }
    }

    public IMapRotationHandler createMapRotationHandler(int rotationInterval) {
        try {
            if (gameMaps.size() >= 2) {
                options.add(Options.MAP_ROTATION);
                this.mapRotationHandler = new MapRotationHandler(this, rotationInterval);
                return mapRotationHandler;
            } else {
                throw new GameSystemException("There are not enough maps available");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IMapVotingHandler createMapVotingHandler() {
        options.add(Options.MAP_INVENTORY);
        this.mapVotingHandler = new MapVotingHandler(this);
        return mapVotingHandler;
    }

    public IMapRotationHandler getMapRotationHandler() {
        try {
            if (mapRotationHandler != null) {
                return mapRotationHandler;
            } else {
                throw new GameSystemException("mapRotationHandler is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IMapVotingHandler getMapVotingHandler() {
        try {
            if (mapVotingHandler != null) {
                return mapVotingHandler;
            } else {
                throw new GameSystemException("mapVotingHandler is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean loadGameWorlds() {
        try {
            if (dir.exists()) {
                if (file.exists()) {
                    this.gameMaps = CoreSystem.getInstance().getGson().fromJson(new FileReader(file), new TypeToken<ArrayList<GameMap>>() {}.getType());
                    return true;
                } else {
                    if (file.createNewFile()) {
                        createGameMapTemplate();
                        return true;
                    } else {
                        GameSystemAPI.getInstance().sendConsoleMessage("§cDie Datei world.json konnte nicht erstellt werden!");
                        return false;
                    }
                }
            } else {
                if (dir.mkdir()) {
                    if (file.createNewFile()) {
                        createGameMapTemplate();
                        return true;
                    } else {
                        GameSystemAPI.getInstance().sendConsoleMessage("§cDie Datei world.json konnte nicht erstellt werden!");
                        return false;
                    }
                } else {
                    GameSystemAPI.getInstance().sendConsoleMessage("§cDie Datei world.json konnte nicht erstellt werden!");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

//    public String parseToJson(final GameMapItem gameWorldItem) {
//        try {
//            if (dir.exists()) {
//                if (file.exists()) {
//                    GameWorldItemHandler gameWorldItemHandler = CoreSystem.getInstance().getGson().fromJson(new FileReader(file), GameWorldItemHandler.class);
//                    gameWorldItemHandler.getGameWorldItems().add(gameWorldItem);
//                    FileWriter fileWriter = new FileWriter(file);
//                    fileWriter.write(CoreSystem.getInstance().getGson().toJson(gameWorldItemHandler));
//                    fileWriter.flush();
//                    fileWriter.close();
//                } else {
//                    log.log(Level.WARNING, ConsoleColor.RED + "Not file found!");
//                }
//            } else {
//                log.log(Level.WARNING, ConsoleColor.RED + "Not dir found!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public String parseToJson(final GameWorldItemHandler gameWorldItemHandler) {
//        try {
//            if (dir.exists()) {
//                if (file.exists()) {
//                    FileWriter fileWriter = new FileWriter(file);
//                    fileWriter.write(CoreSystem.getInstance().getGson().toJson(gameWorldItemHandler));
//                    fileWriter.flush();
//                    fileWriter.close();
//                } else {
//                    log.log(Level.WARNING, ConsoleColor.RED + "Not file found!");
//                }
//            } else {
//                log.log(Level.WARNING, ConsoleColor.RED + "Not dir found!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public GameMap getGameMap(final String world) {
        for (GameMap gameMap : this.gameMaps) {
            if (gameMap.getWorld().equalsIgnoreCase(world)) {
                return gameMap;
            }
        }
        return null;
    }

    private void createGameMapTemplate() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            ArrayList<GameMap> test = new ArrayList<>();
            test.add(new GameMap("world", "spawn", false, new MapItem("world", Material.STONE, new String[]{"world-item", "Build by Server"})));

            fileWriter.write(CoreSystem.getInstance().getGson().toJson(test));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
