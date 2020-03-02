package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreActionBar;
import eu.mcone.coresystem.api.bukkit.util.CoreTitle;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.event.map.MapRotationEvent;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapRotationHandler;
import eu.mcone.gameapi.listener.map.MapRotationListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class GameMapRotationHandler implements MapRotationHandler {

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
        this.currentMap = mapManager.getMaps().get(new Random().nextInt(mapManager.getMaps().size() - 1));
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

        long configLastRotation = mapManager.getConfig().parseConfig().getLastRotation();
        if (configLastRotation > 0) {
            lastRotation = configLastRotation;
        }

        //delaying rotation start as coresystem did not load all players on reload (needed for Messager messages in resumeRotation())
        Bukkit.getScheduler().runTaskLater(mapManager.getSystem(), () -> {
            long newConfigRation = configLastRotation + rotationInterval;
            if (newConfigRation <= ((System.currentTimeMillis() / 1000) + 15)) {
                rotate();
            } else {
                resumeRotation(newConfigRation - (System.currentTimeMillis() / 1000));
            }
        }, 5 * 20);
    }

    private void resumeRotation(long doRotationIn) {
        currentTask = Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                mapManager.getSystem().getMessager().send(p, "§fDie Map wird in §n1 Minute§r§f gewechselt!");
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    mapManager.getSystem().getMessager().send(p, "§fDie Map wird in §n10 Sekunden§r§f gewechselt!");
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

    private void rotate() throws IllegalStateException {
        if (currentTask != null) {
            currentTask.cancel();
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
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }

        Bukkit.getPluginManager().callEvent(new MapRotationEvent(oldMap, currentMap));
        lastRotation = System.currentTimeMillis() / 1000;

        MapsConfig config = mapManager.getConfig().parseConfig();
        config.setLastRotation(lastRotation);
        mapManager.getConfig().updateConfig(config);

        if (newInterval > -1) {
            rotationInterval = newInterval;
            newInterval = -1;
        }
        resumeRotation(rotationInterval);
    }

}
