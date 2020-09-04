/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.listener.backpack.handler.GadgetScheduler;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class DoubleJumpListener extends GadgetListener {

    public DoubleJumpListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    public static boolean isJumping = false;

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.DOUBLEJUMP.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();

            if (isJumping) {
                GameAPI.getInstance().getMessenger().send(p, "§4Bitte warte kurz..");
                return;
            }
            isJumping = true;
            p.getInventory().remove(p.getItemInHand());


            Vector vec = p.getLocation().getDirection().normalize();
            vec = vec.setY(Math.max(0.4000000059604645D, vec.getY())).multiply(1.7F);
            p.setVelocity(vec);

            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            p.playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 10);


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

                                                isJumping = false;

                                                if (p.hasPermission("lobby.silenthub")) {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), DefaultItem.DOUBLEJUMP.getItemStack());
                                                    p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                                                    p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 100, 100);
                                                } else {
                                                    p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), DefaultItem.DOUBLEJUMP.getItemStack());
                                                    p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                                                    p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 3, 100, 100);
                                                }
                                                handler.remove(this);
                                            }, 15);
                                        }
                                    });

                                    handler.remove(this);
                                }, 10);
                            }
                        });

                        handler.remove(this);
                    }, 40);
                }
            });

        }
    }

}
