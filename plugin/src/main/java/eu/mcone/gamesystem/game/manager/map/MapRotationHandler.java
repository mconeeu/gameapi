/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.event.GameMapChangeEvent;
import eu.mcone.gamesystem.api.game.event.GameMapCountdownChangeEvent;
import eu.mcone.gamesystem.api.game.manager.map.GameMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class MapRotationHandler implements eu.mcone.gamesystem.api.game.manager.map.MapRotationHandler {

    private MapManager mapManager;
    private int rotationInterval;
    private int bukkitTask;

    @Getter
    private GameMap currentGameMap;
    @Getter
    private CoreWorld currentCoreWorld;


    public MapRotationHandler(final MapManager mapManager, final int rotationInterval) {
        this.mapManager = mapManager;
        this.rotationInterval = rotationInterval;

        GameTemplate.getInstance().sendConsoleMessage("§aStart map rotation...");

        try {
            if (!mapManager.getGameMaps().isEmpty()) {
                GameMap firstGameMap = mapManager.getGameMaps().get(0);
                CoreWorld coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(firstGameMap.getWorld());
                if (coreWorld != null) {
                    this.currentGameMap = mapManager.getGameMaps().get(0);
                    this.currentCoreWorld = coreWorld;
                    GameTemplate.getInstance().sendConsoleMessage("§cSet currentWorld to §7" + firstGameMap.getWorld());
                } else {
                    throw new GameSystemException("Core world with the name " + firstGameMap.getWorld() + " not found");
                }
            } else {
                throw new GameSystemException("No game maps found!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }
    }

    public void startRotation() {
        if (!Bukkit.getScheduler().isCurrentlyRunning(bukkitTask)) {
            AtomicInteger seconds = new AtomicInteger(rotationInterval);

            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(GameTemplate.getInstance(), () -> {
                seconds.getAndDecrement();

                Bukkit.getServer().getPluginManager().callEvent(
                        new GameMapCountdownChangeEvent
                                (
                                        seconds.get(),
                                        true,
                                        bukkitTask
                                )
                );

                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    //     10 minuten                5 Minuten              2 Minuten               1 Minute                30 Secunden
                    if (seconds.get() == 600 || seconds.get() == 300 || seconds.get() == 120 || seconds.get() == 60 || seconds.get() == 30 || seconds.get() == 15 || seconds.get() == 10 || seconds.get() == 5 || seconds.get() == 1) {
                        int min = Math.round(seconds.get() / 60);
                        if (min > 1) {
                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                CoreSystem.getInstance().createActionBar().message("§7§oMap wird in §a§o" + min + " §7§oMinute(n) gewechselt!").send(player);
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                            });
                        } else {
                            Bukkit.getOnlinePlayers().forEach((player) -> {
                                CoreSystem.getInstance().createActionBar().message("§7§oMap wird in §a§o" + seconds.get() + " §7§oSekunde(n) gewechselt!").send(player);
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                            });
                        }
                    } else if (seconds.get() == 0) {
                        seconds.set(rotationInterval);

                        int i = 0;
                        for (GameMap gameMapItem : mapManager.getGameMaps()) {
                            if (currentGameMap != null) {
                                if (gameMapItem.getWorld().equalsIgnoreCase(currentGameMap.getWorld())) {
                                    i++;

                                    if (mapManager.getGameMaps().size() != i && mapManager.getGameMaps().get(i) != null) {
                                        currentGameMap = mapManager.getGameMaps().get(i);
                                        break;
                                    } else {
                                        currentGameMap = mapManager.getGameMaps().get(0);
                                        break;
                                    }
                                } else {
                                    i++;
                                }
                            } else {
                                currentGameMap = mapManager.getGameMaps().get(0);
                                break;
                            }
                        }

                        CoreWorld coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(currentGameMap.getWorld());

                        if (coreWorld != null) {
                            currentCoreWorld = coreWorld;

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                CoreSystem.getInstance().createTitle().fadeIn(2).fadeOut(5).title("§8» " + currentGameMap.getMapItem().getDisplayName() + " §8«").subTitle("§a§oDie Map wurde gewechselt!").stay(2).send(player);
                                coreWorld.teleportSilently(player, currentGameMap.getSpawnLocation());
                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                            }

                            Bukkit.getPluginManager().callEvent(
                                    new GameMapChangeEvent(
                                            currentGameMap
                                    )
                            );
                        } else {
                            GameTemplate.getInstance().sendConsoleMessage("§cError world is null, please check if the world exists");
                        }
                    }
                }
            }, 0, 20L).getTaskId();
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cThe rotation scheduler is already running...");
        }
    }

    public void stopRotation() {
        if (Bukkit.getScheduler().isCurrentlyRunning(bukkitTask)) {
            Bukkit.getScheduler().cancelTask(bukkitTask);

            GameTemplate.getInstance().sendConsoleMessage("§aStop map rotation task...");
        }
    }
}
