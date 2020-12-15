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
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EasterGunListener extends GadgetListener {

    public EasterGunListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.EASTERGUN.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();

            Egg egg = p.getWorld().spawn(p.getEyeLocation(), Egg.class);
            egg.setVelocity(p.getPlayer().getLocation().getDirection().multiply(1.2));
            egg.setShooter(p);
            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);

            p.getWorld().playEffect(egg.getLocation(), Effect.LARGE_SMOKE, 10);


            Sound.click(p);
            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Sound.tick(p);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Sound.done(p);
                            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.EASTERGUN.getItemStack());

                        handler.cleanup(e);
                    }, 10));
                }, 10));
            }, 10));
        }
    }

    @EventHandler
    public void on(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player && e.getEntityType().equals(EntityType.EGG)) {
            Player p = (Player) e.getEntity().getShooter();

            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 100);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 100);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 100);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 900);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 9000);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LAVA_POP, 900);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.LARGE_SMOKE, 100);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.HEART, 100);
            p.getWorld().playEffect(e.getEntity().getLocation(), Effect.HEART, 100);
        }
    }

    @EventHandler
    public void on(CreatureSpawnEvent e) {
        if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG)) {
            e.setCancelled(true);
        }
    }


}
