/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.handler;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.backpack.handler.TrailHandler;
import eu.mcone.gameapi.listener.backpack.TrailListener;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class GameTrailHandler implements TrailHandler {

    private final GamePlugin plugin;

    private final BukkitTask task;
    private final HashMap<Player, DefaultItem> trails = new HashMap<>();

    public GameTrailHandler(GamePlugin plugin) {
        plugin.registerEvents(new TrailListener(this));

        this.plugin = plugin;
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (final HashMap.Entry<Player, DefaultItem> trailEntry : trails.entrySet()) {
                switch (trailEntry.getValue()) {
                    case TRAIL_COOKIES: {
                        break;
                    }
                    case TRAIL_GLOW:
                        break;
                    case TRAIL_ENDER:
                        break;
                    case TRAIL_MUSIC:
                        break;
                    case TRAIL_HEART:
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.HEART, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.HEART, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SPELL, 5);
                        break;
                    case TRAIL_LAVA:
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.LAVA_POP, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.LAVA_POP, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.LARGE_SMOKE, 5);
                        break;
                    case TRAIL_SNOW:
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOW_SHOVEL, 2);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOW_SHOVEL, 2);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOW_SHOVEL, 2);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOWBALL_BREAK, 10);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOWBALL_BREAK, 10);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOWBALL_BREAK, 10);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOWBALL_BREAK, 10);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SNOWBALL_BREAK, 10);
                        break;
                    case TRAIL_WATER:
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SPLASH, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.SPLASH, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.WATERDRIP, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.WATERDRIP, 5);
                        trailEntry.getKey().getLocation().getWorld().playEffect(trailEntry.getKey().getLocation(), Effect.WATERDRIP, 5);
                        break;
                }
            }
        }, 100L, 3);
    }

    @Override
    public void setTrail(Player p, BackpackItem trail) {
        DefaultItem current = this.trails.get(p);
        DefaultItem choosed = DefaultItem.getItemByID(DefaultCategory.TRAIL, trail.getId());

        if (current != null && current.equals(choosed)) {
            p.closeInventory();
        } else {
            if (choosed != null) {
                this.trails.put(p, choosed);
                Sound.done(p);
                p.closeInventory();
            } else {
                throw new IllegalStateException("Could not set Trail from item "+trail.getName()+". Item is not a trail!");
            }
        }
    }

    @Override
    public void removeTrail(Player p) {
        if (this.trails.get(p) != null) {
            plugin.getMessenger().send(p, "??7Trail entfernt!");
            this.trails.remove(p);
            p.closeInventory();
        }
    }

    public void stop() {
        task.cancel();
    }

    public void unregisterPlayer(Player p) {
        trails.remove(p);
    }

}
