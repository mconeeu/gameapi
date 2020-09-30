package eu.mcone.gameapi.listener.backpack.handler;

import org.bukkit.scheduler.BukkitTask;

public interface BackpackSchedulerProvider {

    BukkitTask register();

}
