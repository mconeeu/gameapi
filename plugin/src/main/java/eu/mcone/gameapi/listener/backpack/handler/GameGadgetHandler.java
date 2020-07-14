package eu.mcone.gameapi.listener.backpack.handler;

import eu.mcone.gameapi.api.backpack.handler.GadgetHandler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class GameGadgetHandler implements GadgetHandler {

    private final HashMap<GadgetScheduler, BukkitTask> schedulers;

    public GameGadgetHandler() {
        schedulers = new HashMap<>();
    }

    public void register(GadgetScheduler scheduler) {
        this.schedulers.put(scheduler, scheduler.register());
    }

    public void remove(GadgetScheduler scheduler) {
        this.schedulers.remove(scheduler);
    }

    public void stop() {
        for (BukkitTask task : schedulers.values()) {
            task.cancel();
        }
    }
}
