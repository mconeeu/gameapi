/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.util.CoreActionBar;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.event.map.MapRotationEvent;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapRotationHandler;
import eu.mcone.gameapi.listener.map.MapRotationListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class GameMapRotationHandler implements MapRotationHandler {

    private static final Random MAP_RANDOM = new Random();
    private final GameMapManager mapManager;

    private BukkitTask currentTask, countdownTask;
    @Getter
    private long rotationInterval = 0, lastRotation = 0, newInterval = -1;
    private int countdown = 0;

    @Getter
    private GameAPIMap currentMap;

    public GameMapRotationHandler(GameMapManager mapManager) throws IllegalArgumentException {
        mapManager.getSystem().sendConsoleMessage("§aLoading Map RotationHandler...");
        mapManager.getSystem().registerEvents(new MapRotationListener(this));

        if (mapManager.getMaps().size() < 2) {
            throw new IllegalArgumentException("Cannot initialize MapRotationHandler: more than one Map must be loaded!");
        }

        this.mapManager = mapManager;
    }

    @Override
    public GameMapRotationHandler setRotationInterval(long rotationInterval) throws IllegalArgumentException {
        if (rotationInterval >= 60) {
            if (this.rotationInterval >= 60) {
                this.newInterval = rotationInterval;
            } else {
                this.rotationInterval = rotationInterval;
            }
        } else {
            throw new IllegalArgumentException("Cannot set RotationInterval: rotationInterval must be greater then or equal to 60 seconds!");
        }

        return this;
    }

    @Override
    public void startRotation() {
        if (rotationInterval < 60) {
            rotationInterval = 600;
        }
        if (lastRotation != 0) {
            throw new IllegalStateException("Cannot start rotation. The Rotation is already initialized and will happen in " + (rotationInterval - (System.currentTimeMillis() / 1000) - lastRotation) + " seconds!");
        }

        MapsConfig config = mapManager.getConfig().parseConfig();
        if (config.getLastRotation() > 0) {
            lastRotation = config.getLastRotation();
        }

        long newConfigRation = config.getLastRotation() + rotationInterval;
        if (newConfigRation <= ((System.currentTimeMillis() / 1000) + 15) || config.getCurrentMap() == null || mapManager.getMap(config.getCurrentMap()) == null) {
            rotate();
        } else {
            currentMap = mapManager.getMap(config.getCurrentMap());

            //delaying rotation start as coresystem did not load all players on reload (needed for Messager messages in resumeRotation())
            Bukkit.getScheduler().runTaskLater(mapManager.getSystem(), () -> {
                mapManager.getSystem().sendConsoleMessage("§7Starting RotationHandler with old Map " + currentMap.getName());
                resumeRotation(newConfigRation - (System.currentTimeMillis() / 1000));
            }, 5 * 20);
        }
    }

    private void resumeRotation(long doRotationIn) {
        currentTask = Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Msg.send(p, "§fDie Map wird in §n1 Minute§r§f gewechselt!");
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Msg.send(p, "§fDie Map wird in §n10 Sekunden§r§f gewechselt!");
                }

                countdown = 10;
                countdownTask = Bukkit.getScheduler().runTaskTimerAsynchronously(mapManager.getSystem(), () -> {
                    if (countdownTask != null && countdown <= 0) {
                        countdownTask.cancel();
                        return;
                    }

                    CoreActionBar msg = CoreSystem.getInstance().createActionBar().message("§7§oMap wird in §f§o" + countdown + " Sekunde" + (countdown != 0 ? "n" : "") + "§7§o gewechselt!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        msg.send(p);
                    }
                    countdown--;
                }, 0L, 20L);
                Bukkit.getScheduler().runTaskLater(mapManager.getSystem(), this::rotate, 10 * 20);
            }, 50 * 20);
        }, (doRotationIn * 20) - (60 * 20));
    }

    public void rotate() throws IllegalStateException {
        if (currentTask != null) {
            currentTask.cancel();
        }
        if (currentMap == null) {
            this.currentMap = mapManager.getMaps().get(MAP_RANDOM.nextInt(mapManager.getMaps().size() - 1));
        }

        int lastIndex = -1, newIndex = 0;
        for (int i = 0; i < mapManager.getMaps().size(); i++) {
            if (mapManager.getMaps().get(i).equals(currentMap)) {
                lastIndex = i;
                break;
            }
        }
        if ((lastIndex + 1) <= (mapManager.getMaps().size() - 1)) {
            newIndex = lastIndex + 1;
        }

        GameAPIMap oldMap = currentMap;
        currentMap = mapManager.getMaps().get(newIndex);
        CoreWorld coreWorld = currentMap.getWorld();

        CoreTitle title = CoreSystem.getInstance().createTitle()
                .title("§8» §f§o" + currentMap.getWorld().getName() + " §8«")
                .subTitle("§a§oDie Map wurde gewechselt!")
                .stay(4)
                .fadeIn(2)
                .fadeOut(2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            title.send(player);
            coreWorld.teleportSilently(player, "spawn");
            Sound.teleport(player);
        }

        Bukkit.getPluginManager().callEvent(new MapRotationEvent(oldMap, currentMap));
        lastRotation = System.currentTimeMillis() / 1000;

        MapsConfig config = mapManager.getConfig().parseConfig();
        config.setLastRotation(lastRotation);
        config.setCurrentMap(currentMap.getName());
        mapManager.getConfig().updateConfig(config);

        if (newInterval > -1) {
            rotationInterval = newInterval;
            newInterval = -1;
        }

        mapManager.getSystem().sendConsoleMessage("§7Changing Map to " + currentMap.getName());
        resumeRotation(rotationInterval);
    }

    @Override
    public String getFormattedTimeUntilNextRotation() {
        String date = "...";

        long difference = rotationInterval - ((System.currentTimeMillis() / 1000) - lastRotation);
        if (difference <= rotationInterval) {
            Calendar differenceDate = Calendar.getInstance(TimeZone.getTimeZone("CEST"));
            differenceDate.setTimeInMillis(difference * 1000);

            if (differenceDate.get(Calendar.HOUR) >= 1) {
                date = differenceDate.get(Calendar.HOUR) + 1 + " Stunden";
            } else if (differenceDate.get(Calendar.MINUTE) >= 1) {
                date = differenceDate.get(Calendar.MINUTE) + 1 + " Minuten";
            } else if (differenceDate.get(Calendar.SECOND) >= 1) {
                date = differenceDate.get(Calendar.SECOND) + " Sekunden";
            }
        }

        return date;
    }
}
