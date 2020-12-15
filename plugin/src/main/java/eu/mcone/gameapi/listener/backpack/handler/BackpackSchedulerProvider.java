/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.handler;

import org.bukkit.scheduler.BukkitTask;

public interface BackpackSchedulerProvider {

    BukkitTask register();

}
