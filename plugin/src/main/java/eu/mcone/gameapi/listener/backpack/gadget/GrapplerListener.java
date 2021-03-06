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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class GrapplerListener extends GadgetListener {

    public GrapplerListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();

        Fish h = e.getHook();

        if (((e.getState().equals(PlayerFishEvent.State.IN_GROUND)) ||
                (e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) ||
                (e.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT))) &&
                (p.getItemInHand().equals(DefaultItem.GRAPPLING_HOOK.getItemStack())) &&
                (Bukkit.getWorld(e.getPlayer().getWorld().getName())
                        .getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1,
                                h.getLocation().getBlockZ())
                        .getType() != Material.AIR)) {
            if (Bukkit.getWorld(e.getPlayer().getWorld().getName())
                    .getBlockAt(h.getLocation().getBlockX(), h.getLocation().getBlockY() - 1,
                            h.getLocation().getBlockZ())
                    .getType() != Material.STATIONARY_WATER) {
                Location lc = p.getLocation();
                Location to = e.getHook().getLocation();

                lc.setY(lc.getY() + 0.5D);
                p.teleport(lc);

                Sound.play(p, org.bukkit.Sound.ENDERDRAGON_WINGS);
                double g = -0.08D;
                double t = to.distance(lc);
                double v_x = (1.0D + 0.07D * t) * (to.getX() - lc.getX()) / t;
                double v_y = (1.0D + 0.03D * t) * (to.getY() - lc.getY()) / t - 0.5D * g * t;
                double v_z = (1.0D + 0.07D * t) * (to.getZ() - lc.getZ()) / t;

                Vector v = p.getVelocity();
                v.setX(v_x);
                v.setY(v_y);
                v.setZ(v_z);
                p.setVelocity(v);
            }
        }

    }
}
