/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.handler;

import eu.mcone.gameapi.api.backpack.handler.GadgetHandler;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GameGadgetHandler implements GadgetHandler {

    private final Map<Event, Set<BukkitTask>> schedulers;

    public GameGadgetHandler() {
        schedulers = new HashMap<>();
    }

    public void register(Event event, BackpackSchedulerProvider provider) {
        if (schedulers.containsKey(event)) {
            schedulers.get(event).add(provider.register());
        } else {
            schedulers.put(event, new HashSet<>(Collections.singleton(provider.register())));
        }
    }

    public void cleanup(Event event) {
        schedulers.remove(event);
    }

    public void stop() {
        for (Set<BukkitTask> schedulers : schedulers.values()) {
            for (BukkitTask task : schedulers) {
                task.cancel();
            }
        }
        schedulers.clear();
    }
}
