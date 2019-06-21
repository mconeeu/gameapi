/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.map;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.event.GameMapChangeEvent;
import eu.mcone.gamesystem.api.game.manager.map.GameMapItem;
import eu.mcone.gamesystem.api.game.manager.map.GameWorldItemHandler;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.game.inventory.inventories.MapInventory;
import eu.mcone.networkmanager.core.api.console.ConsoleColor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapManager implements IMapManager {

    private Logger log;

    @Getter
    private final CorePlugin instance;
    @Getter
    private GameMapItem gainedMap;
    @Getter
    @Setter
    private CoreWorld currentWorld;

    private int rotationInterval;

    @Getter
    private List<Options> options;
    @Getter
    private List<GameMapItem> gameWorldItems;
    @Getter
    private Map<String, Integer> mapPopularity;
    @Getter
    private Map<Player, String> mapVoting;

    private File dir;
    private File file;

    public MapManager(final CorePlugin instance, final Options... options) {
        log = GameSystemAPI.getInstance().getLogger();
        this.instance = instance;

        this.options = Arrays.asList(options);
        gameWorldItems = new ArrayList<>();
        mapPopularity = new HashMap<>();
        mapVoting = new HashMap<>();

        dir = new File("./plugins/" + GameTemplate.getInstance().getPluginName());
        file = new File(dir, "/worlds.json");

        if (loadGameWorldItems()) {
            log.info("Es wurden alle Items geladen!");
        } else {
            log.log(Level.SEVERE, ConsoleColor.RED + "Es konnten nicht alle Items geladen werden!");
        }
    }

    public void useMapRotation(final int rotationInterval) {
        if (options.contains(Options.MAP_ROTATION)) {
            this.rotationInterval = rotationInterval;
        } else {
            options.add(Options.MAP_ROTATION);
            this.rotationInterval = rotationInterval;
        }
    }

    public boolean loadGameWorldItems() {
        try {
            if (dir.exists()) {
                if (file.exists()) {
                    GameWorldItemHandler gameWorldItemHandler = CoreSystem.getInstance().getGson().fromJson(new FileReader(file), GameWorldItemHandler.class);
                    this.gameWorldItems = gameWorldItemHandler.getGameWorldItems();

                    if (options.contains(Options.MAP_ROTATION)) {
                        if (gameWorldItems.size() >= 2) {
                            log.info("Start map rotation...");
                            new MapRotationHandler(this, rotationInterval);
                        } else {
                            log.log(Level.SEVERE, ConsoleColor.RED + "There are not enough maps available");
                        }
                    }
                    return true;
                } else {
                    if (file.createNewFile()) {
                        createGameWorldHandler();
                        return true;
                    } else {
                        log.log(Level.WARNING, ConsoleColor.RED + "Cannot create file...");
                        return false;
                    }
                }
            } else {
                if (dir.mkdir()) {
                    if (file.createNewFile()) {
                        createGameWorldHandler();
                        return true;
                    } else {
                        log.log(Level.WARNING, ConsoleColor.RED + "Cannot create file...");
                        return false;
                    }
                } else {
                    log.log(Level.WARNING, ConsoleColor.RED + "Cannot create dir...");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String parseToJson(final GameMapItem gameWorldItem) {
        try {
            if (dir.exists()) {
                if (file.exists()) {
                    GameWorldItemHandler gameWorldItemHandler = CoreSystem.getInstance().getGson().fromJson(new FileReader(file), GameWorldItemHandler.class);
                    gameWorldItemHandler.getGameWorldItems().add(gameWorldItem);
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(CoreSystem.getInstance().getGson().toJson(gameWorldItemHandler));
                    fileWriter.flush();
                    fileWriter.close();
                } else {
                    log.log(Level.WARNING, ConsoleColor.RED + "Not file found!");
                }
            } else {
                log.log(Level.WARNING, ConsoleColor.RED + "Not dir found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String parseToJson(final GameWorldItemHandler gameWorldItemHandler) {
        try {
            if (dir.exists()) {
                if (file.exists()) {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(CoreSystem.getInstance().getGson().toJson(gameWorldItemHandler));
                    fileWriter.flush();
                    fileWriter.close();
                } else {
                    log.log(Level.WARNING, ConsoleColor.RED + "Not file found!");
                }
            } else {
                log.log(Level.WARNING, ConsoleColor.RED + "Not dir found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GameMapItem getMap(final String world) {
        for (GameMapItem mapItems : this.gameWorldItems) {
            if (mapItems.getWorld().equalsIgnoreCase(world)) {
                return mapItems;
            }
        }
        return null;
    }

    public GameMapItem closeVoting() {
        if (!options.contains(Options.MAP_ROTATION)) {
            if (mapVoting.size() != 0) {
                for (int i = 0; i <= mapVoting.size(); i++) {
                    for (Map.Entry<String, Integer> entry : this.mapPopularity.entrySet()) {
                        if (entry.getValue() < i || entry.getValue() == i) {
                            gainedMap = getMap(entry.getKey());
                            log.info(ConsoleColor.GREEN + "Return the world `" + entry.getKey() + "`");
                            return gainedMap;
                        }
                    }
                }
            } else {
                gainedMap = gameWorldItems.get(new Random().nextInt(gameWorldItems.size()));
                log.info(ConsoleColor.GREEN + "Return the random map `" + gainedMap.getWorld() + "`");
                return gainedMap;
            }

            Bukkit.getServer().getPluginManager().callEvent(
                    new GameMapChangeEvent(
                            gainedMap
                    )
            );
        } else {
            instance.sendConsoleMessage("§cSorry can not return a Map Item because the Map Rotation option is activated");
        }

        return null;
    }

    public void createMapInventory(final Player player, CoreInventory returnInventory) {
        if (options.contains(Options.MAP_INVENTORY)) {
            new MapInventory().createInventory(player, this);
        } else if (options.contains(Options.MAP_ROTATION)) {
            //TODO: Add inventory for map rotation option
        }
    }

    private void createGameWorldHandler() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(CoreSystem.getInstance().getGson().toJson(
                    new GameWorldItemHandler(
                            new ArrayList<GameMapItem>() {{
                                add(new GameMapItem(
                                        "Test",
                                        "Test",
                                        true,
                                        Material.BARRIER,
                                        new String[]
                                                {
                                                        "Test",
                                                        "Test"
                                                }));
                            }})
            ));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
