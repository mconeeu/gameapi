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
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LoveGunListener extends GadgetListener {

    public LoveGunListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.LOVEGUN.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();

            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 29, 25);
                all.spigot().playEffect(p.getLocation(), Effect.HEART, 1, 1, 1, 1, 1, 3, 78, 42);
            }
            World w = p.getWorld();


            w.playEffect(p.getLocation(), Effect.FLAME, 1);
            w.playEffect(p.getLocation(), Effect.HEART, 10);

            Sound.tick(p);

            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Sound.tick(p);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Sound.done(p);

                            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.LOVEGUN.getItemStack());

                        handler.cleanup(e);
                    }, 9));
                }, 10));
            }, 13));
        }
    }

}
