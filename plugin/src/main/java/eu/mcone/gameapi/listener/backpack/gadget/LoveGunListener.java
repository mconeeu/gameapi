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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

public class LoveGunListener extends GadgetListener {

    public LoveGunListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.LOVEGUN.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();


            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
            } else {
                p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
            }

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 29, 25);
                all.spigot().playEffect(p.getLocation(), Effect.HEART, 1, 1, 1, 1, 1, 3, 78, 42);
            }
            World w = p.getWorld();


            w.playEffect(p.getLocation(), Effect.FLAME, 1);
            w.playEffect(p.getLocation(), Effect.HEART, 10);

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
                                    p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                                    handler.register(new GadgetScheduler() {
                                        @Override
                                        public BukkitTask register() {
                                            return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);

                                                if (p.hasPermission("lobby.silenthub")) {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), DefaultItem.LOVEGUN.getItemStack());
                                                } else {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), DefaultItem.LOVEGUN.getItemStack());
                                                }

                                                handler.remove(this);
                                            }, 9);
                                        }
                                    });

                                    handler.remove(this);
                                }, 10);
                            }
                        });

                        handler.remove(this);
                    }, 13);
                }
            });
        }
    }

}
