/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.listener.backpack.handler.GadgetScheduler;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

public class SnowGunListener extends GadgetListener {

    public SnowGunListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.SNOWGUN.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();

            Snowball snowball = p.getWorld().spawn(p.getEyeLocation(), Snowball.class);
            snowball.setVelocity(p.getPlayer().getLocation().getDirection());
            snowball.setShooter(p);

            p.getWorld().playEffect(snowball.getLocation(), Effect.LARGE_SMOKE, 10);

            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
            } else {
                p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
            }

            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
            handler.register(new GadgetScheduler() {
                @Override
                public BukkitTask register() {
                    return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                        handler.register(new GadgetScheduler() {
                            @Override
                            public BukkitTask register() {
                                return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                                    handler.register(new GadgetScheduler() {
                                        @Override
                                        public BukkitTask register() {
                                            return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
                                                if (p.hasPermission("lobby.silenthub")) {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), DefaultItem.SNOWGUN.getItemStack());
                                                } else {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), DefaultItem.SNOWGUN.getItemStack());
                                                }

                                                handler.remove(this);
                                            }, 10);
                                        }
                                    });

                                    handler.remove(this);
                                }, 10);
                            }
                        });

                        handler.register(this);
                    }, 10);
                }
            });
        }
    }

    @EventHandler
    public void on(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player && e.getEntityType().equals(EntityType.SNOWBALL)) {
            for (Player all : Bukkit.getOnlinePlayers()) {

                all.spigot().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 1, 1, 1, 1, 1, 2, 50, 45);
                all.spigot().playEffect(e.getEntity().getLocation(), Effect.LARGE_SMOKE, 1, 1, 1, 1, 1, 2, 90, 55);
            }
        }
    }

}