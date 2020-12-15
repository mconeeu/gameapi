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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnderPearlListener extends GadgetListener {

    public EnderPearlListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.ENDERPEARL.getItemStack()) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            Player p = e.getPlayer();

            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);
            p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
            Sound.tick(p);

            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Sound.tick(p);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.ENDERPEARL.getItemStack());

                        Sound.done(p);
                        p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                        p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 100, 100);
                        p.spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1, 1, 1, 1, 1, 2, 100, 100);

                        handler.cleanup(e);
                    }, 15));
                }, 10));
            }, 10));
        }
    }

    @EventHandler
    public void on(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player && e.getEntityType().equals(EntityType.ENDER_PEARL)) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.spigot().playEffect(e.getEntity().getLocation(), Effect.WITCH_MAGIC, 1, 1, 1, 1, 1, 2, 100, 100);
                all.spigot().playEffect(e.getEntity().getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 4, 100, 100);
            }
        }
    }

}