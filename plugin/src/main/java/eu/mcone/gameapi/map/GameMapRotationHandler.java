package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.util.CoreActionBar;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.event.map.MapRotationEvent;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.api.map.MapRotationHandler;
import eu.mcone.gameapi.listener.MapRotationListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class GameMapRotationHandler implements MapRotationHandler {

    private final GameMapManager mapManager;

    private BukkitTask currentTask, countdownTask;
    private long rotationInterval, lastRotation = 0, newInterval = -1;
    private int countdown = 0;

    @Getter
    private GameAPIMap currentMap;

    public GameMapRotationHandler(GameMapManager mapManager) throws IllegalArgumentException {
        this(mapManager, 600);
    }

    public GameMapRotationHandler(GameMapManager mapManager, long rotationInterval) throws IllegalArgumentException {
        mapManager.getSystem().registerEvents(new MapRotationListener(this));

        if (rotationInterval > 60) {
            this.rotationInterval = rotationInterval;
        } else {
            throw new IllegalArgumentException("Cannot initialize MapRotationHandler: rotationInterval must be greater then 60 seconds!");
        }
        this.mapManager = mapManager;
        this.currentMap = mapManager.getMaps().get(new Random().nextInt(mapManager.getMaps().size() - 1));

        rotate();
    }

    @Override
    public void setRotationInterval(long roationInvterval) throws IllegalArgumentException {
        if (rotationInterval > 60) {
            this.newInterval = rotationInterval;

            if (Math.abs(roationInvterval - this.rotationInterval) > 60) {
                try {
                    rotate();
                } catch (IllegalStateException ignored) { }
            }
        } else {
            throw new IllegalArgumentException("Cannot initialize MapRotationHandler: rotationInterval must be greater then 60 seconds!");
        }
    }

    public void resumeRotation() {
        currentTask = Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                mapManager.getSystem().getMessager().send(p, "§fDie Map wird in §l1 Minute§r§f gewechselt!");
            }

            Bukkit.getScheduler().runTaskLaterAsynchronously(mapManager.getSystem(), () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    mapManager.getSystem().getMessager().send(p, "§fDie Map wird in §l10 Sekunden§r§f gewechselt!");
                }

                countdown = 10;
                countdownTask = Bukkit.getScheduler().runTaskTimerAsynchronously(mapManager.getSystem(), () -> {
                    if (countdownTask != null && countdown <= 0) {
                        countdownTask.cancel();
                        return;
                    }

                    CoreActionBar msg = CoreSystem.getInstance().createActionBar().message("§7§oMap wird in §f§o" + countdown + " Sekunden(n)§7§o gewechselt!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        msg.send(p);
                    }
                }, 0L, 20L);
                Bukkit.getScheduler().runTaskLater(mapManager.getSystem(), this::rotate, 10 * 20);
            }, 50 * 20);
        }, (rotationInterval * 20) - (60 * 20));
    }

    @Override
    public void rotate() throws IllegalStateException {
        final long taskRunning = (System.currentTimeMillis() / 1000) - lastRotation;

        if (taskRunning > (rotationInterval - 60) && taskRunning <= rotationInterval) {
            throw new IllegalStateException("Cannot rotate. The Rotation is already initialized and will happen in " + (rotationInterval - taskRunning) + " seconds!");
        } else if (taskRunning < (rotationInterval - 60)) {
            currentTask.cancel();
        }

        GameAPIMap oldMap = currentMap;
        int lastIndex = 0;
        int newIndex = 0;

        for (int i = 0; i < mapManager.getMaps().size() - 1; i++) {
            if (mapManager.getMaps().get(i).equals(currentMap)) {
                lastIndex = i;
            }
        }

        if (lastIndex + 1 <= mapManager.getMaps().size() - 1) {
            newIndex = lastIndex + 1;
        }

        this.currentMap = mapManager.getMaps().get(newIndex);
        CoreWorld coreWorld = currentMap.getWorld();

        for (Player player : Bukkit.getOnlinePlayers()) {
            CoreSystem.getInstance().createTitle().fadeIn(2).fadeOut(5).title("§8» " + currentMap.getItem().getItemMeta().getDisplayName() + " §8«").subTitle("§a§oDie Map wurde gewechselt!").stay(2).send(player);
            coreWorld.teleportSilently(player, "spawn");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }

        Bukkit.getPluginManager().callEvent(new MapRotationEvent(oldMap, currentMap));
        lastRotation = System.currentTimeMillis() / 1000;

        if (newInterval > -1) {
            rotationInterval = newInterval;
            newInterval = -1;
        }
        resumeRotation();
    }

}
