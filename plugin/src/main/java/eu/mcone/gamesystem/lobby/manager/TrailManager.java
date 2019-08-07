/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */


package eu.mcone.gamesystem.lobby.manager;

import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.enums.Item;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TrailManager implements eu.mcone.gamesystem.api.lobby.manager.TrailManager {

    private BukkitTask task;
    private HashMap<Player, Item> trails = new HashMap<>();

    public TrailManager() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(GameSystem.getInstance(), () -> {
            for (final HashMap.Entry<Player, Item> trailEntry : trails.entrySet()) {
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

    public void shutdown() {
        this.task.cancel();
    }

    public void setTrail(Player p, Item trail) {
        Item entry = this.trails.get(p);

        if (entry != null && entry == trail) {
            p.closeInventory();
        } else {
            this.trails.put(p, trail);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            p.closeInventory();
        }
    }

    public void removeTrail(Player p) {
        if (this.trails.get(p) != null) {
            GameTemplate.getInstance().getMessager().send(p, "§7Trail entfernt!");
            this.trails.remove(p);
            p.closeInventory();
        }
    }

    public void unregisterPlayer(Player p) {
        trails.remove(p);
    }

}
