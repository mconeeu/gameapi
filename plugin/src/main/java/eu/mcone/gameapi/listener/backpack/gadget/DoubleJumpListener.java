/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
                Msg.send(p, "§4Bitte warte kurz..");
                return;
            }
            isJumping = true;
            p.getInventory().remove(p.getItemInHand());


            Vector vec = p.getLocation().getDirection().normalize();
            vec = vec.setY(Math.max(0.4000000059604645D, vec.getY())).multiply(1.7F);
            p.setVelocity(vec);

            Sound.teleport(p);
            p.playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 10);


            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Sound.tick(p);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Sound.done(p);

                        isJumping = false;

                        p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.DOUBLEJUMP.getItemStack());
                        p.getWorld().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 10);
                        p.spigot().playEffect(p.getLocation(), Effect.FLAME, 1, 1, 1, 1, 1, 2, 100, 100);
                        handler.cleanup(e);
                    }, 15));
                }, 10));
            }, 40));

        }
    }

}
