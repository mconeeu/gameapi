/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.event.GameMapChangeEvent;
import eu.mcone.gamesystem.api.game.manager.map.GameMapItem;
import eu.mcone.networkmanager.core.api.console.ConsoleColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapRotationHandler {

    private Logger log;
    private int bukkitTask;

    private List<GameMapItem> maps;
    private int rotationInterval;
    private GameMapItem currentMapItem;

    public MapRotationHandler(final MapManager mapManager, final int rotationInterval) {
        log = GameSystemAPI.getInstance().getLogger();

        this.maps = mapManager.getGameWorldItems();

        if (mapManager.getOptions().contains(MapManager.Options.MAP_ROTATION)) {
            this.rotationInterval = rotationInterval;
            startRotation();
        } else {
            log.log(Level.SEVERE, ConsoleColor.RED +  "You can not use the Map Rotation manager because the option is not activated");
        }
    }

    private void startRotation() {
        if (!Bukkit.getScheduler().isCurrentlyRunning(bukkitTask)) {
            AtomicInteger seconds = new AtomicInteger(rotationInterval);
            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(GameTemplate.getInstance(), () -> {
                seconds.getAndDecrement();

                //      4 Minuten               3 Minuten               2 Minuten               1 Minute                30 Secunden
                if (seconds.get() == 240 || seconds.get() == 180 || seconds.get() == 120 || seconds.get() == 60 || seconds.get() == 30) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CoreSystem.getInstance().createActionBar().message("§7Map wird in §a" + seconds.get() + " §7Sekunden geändert!").send(player);
                    }
                }  else if (seconds.get() == 0) {
                    seconds.set(rotationInterval);

                    int i = 0;
                    for (GameMapItem gameMapItem : maps) {
                        if (currentMapItem != null) {
                            if (gameMapItem.getWorld().equalsIgnoreCase(currentMapItem.getWorld())) {
                                i++;

                                if (maps.size() != i && maps.get(i) != null) {
                                    currentMapItem = maps.get(i);
                                    break;
                                } else {
                                    currentMapItem = maps.get(0);
                                    break;
                                }
                            } else {
                                i++;
                            }
                        } else {
                            currentMapItem = maps.get(0);
                            break;
                        }
                    }

                    CoreWorld coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(currentMapItem.getWorld());
                    CoreSystem.getInstance().enableSpawnCommand(GameTemplate.getInstance(), coreWorld, 0);

                    Bukkit.getPluginManager().callEvent(
                            new GameMapChangeEvent(
                                    currentMapItem
                            )
                    );

                    if (coreWorld != null) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            CoreSystem.getInstance().createTitle().fadeIn(2).fadeOut(5).title("§8» " + currentMapItem.getDisplayName() + " §8«").subTitle("§aDie Map wurde geändert").stay(2).send(player);
                            GameTemplate.getInstance().getMapManager().setCurrentWorld(coreWorld);
                            coreWorld.teleportSilently(player, "spawn");
                        }
                    } else {
                        log.log(Level.SEVERE, ConsoleColor.RED + "Error world is null, please check if the world exists");
                    }
                }
            }, 0, 20L).getTaskId();
        } else {
            log.log(Level.INFO, "The rotation scheduler is already running...");
        }
    }

    public void stopRotation() {
        if (Bukkit.getScheduler().isCurrentlyRunning(bukkitTask)) {
            Bukkit.getScheduler().cancelTask(bukkitTask);
            log.info("Stop map rotation task...");
        }
    }
}
