/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BombListener extends GadgetListener {

    public BombListener(GamePlugin<?> plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.BOMB.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();
            org.bukkit.entity.Item item = p.getLocation().getWorld().dropItem(p.getEyeLocation(),
                    new ItemStack(Material.MAGMA_CREAM));
            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(3, null);
            } else {
                p.getInventory().setItem(2, null);
            }


            Vector v = p.getLocation().getDirection().multiply(1);
            item.setVelocity(v);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY(),
                        item.getLocation().getZ(), 3, false, false);
                item.getWorld().playEffect(item.getLocation().add(0, 1, 0), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(1, 0, 0), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(0, 0, 1), Effect.LAVA_POP, 1);
                item.getWorld().playEffect(item.getLocation().add(0, 1, 1), Effect.LAVA_POP, 1);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.getLocation().distance(item.getLocation()) <= 6) {
                        Vector v1 = all.getLocation().getDirection().setY(0.8).multiply(1.1);

                        all.setVelocity(v1);
                    }
                }
                item.getWorld().playEffect(item.getLocation(), Effect.FIREWORKS_SPARK, 1);
                item.getWorld().playSound(item.getLocation(), Sound.GLASS, 1, 1);

                Bukkit.getScheduler().runTaskLater(plugin, () -> item.getWorld().playSound(item.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1), 3);
            }, 20);

            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);

                        if (p.hasPermission("lobby.silenthub")) {
                            p.getInventory().setItem(3, DefaultItem.BOMB.getItemStack());
                        } else {
                            p.getInventory().setItem(2, DefaultItem.BOMB.getItemStack());
                        }
                    }, 30);
                }, 10);
            }, 10);
        }
    }

}