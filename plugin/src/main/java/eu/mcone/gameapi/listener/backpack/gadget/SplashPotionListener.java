/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;

public class SplashPotionListener extends GadgetListener {

    public SplashPotionListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    public static ArrayList<Player> splashPotionEffects = new ArrayList<>();

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.SPLASH_POTION.getItemStack()) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            Player p = e.getPlayer();

            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                ItemStack boots = p.getInventory().getBoots();

                Sound.tick(p);
                Sound.play(p, org.bukkit.Sound.GLASS);
                Sound.play(p, org.bukkit.Sound.SPLASH2);

                for (PotionEffect effect : p.getActivePotionEffects()) {
                    p.removePotionEffect(effect.getType());
                }

                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 320, 1));
                p.getInventory().setBoots(null);

                splashPotionEffects.add(p);

                World w = p.getWorld();

                w.playEffect(p.getLocation(), Effect.FLAME, 3);
                w.playEffect(p.getLocation(), Effect.FLAME, 1);
                w.playEffect(p.getLocation(), Effect.WITCH_MAGIC, 5);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);
    /*if (p.hasPermission("lobby.silenthub")) {
        p.getInventory().setItem(3, Item.SPLASH_POTION.getItemStack());
    } else {
        p.getInventory().setItem(2, Item.SPLASH_POTION.getItemStack());
    }*/

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Sound.done(p);

                        p.getInventory().setBoots(boots);

                        if (p.hasPermission("lobby.silenthub")) {
                            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.SPLASH_POTION.getItemStack());

                            splashPotionEffects.remove(p);

                            p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                            p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 10);
                            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 10);
                            p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 100, 100);
                            p.spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1, 1, 1, 1, 1, 2, 100, 100);

                        } else {
                            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.SPLASH_POTION.getItemStack());
                            p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                            p.getWorld().playEffect(p.getLocation(), Effect.FLAME, 10);
                            p.getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 10);
                            p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 1, 100, 100);
                            p.spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1, 1, 1, 1, 1, 2, 100, 100);
                            splashPotionEffects.remove(p);
                        }

                        handler.cleanup(e);
                    }, 220));
                }, 95));
            }, 4));
        }
    }

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (splashPotionEffects.contains(p)) {
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SPELL, 9);
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SPELL, 9);
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SPELL, 9);
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SPELL, 9);
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.SPELL, 2);
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.FIREWORKS_SPARK, 5);


        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();

        if (projectile instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) projectile;
            Collection<PotionEffect> effects = pot.getEffects();
            for (PotionEffect p : effects) {
                if (p.getType().equals(PotionEffectType.INVISIBILITY)) {
                    projectile.getLocation().getWorld().playEffect(projectile.getLocation(), Effect.POTION_BREAK, 5);
                    projectile.getLocation().getWorld().playEffect(projectile.getLocation(), Effect.POTION_SWIRL, 5);
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        splashPotionEffects.remove(e.getPlayer());
    }

}

