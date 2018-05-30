/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.GameSystem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        Entity byEnt = e.getDamager();

        if (ent instanceof Player) {
            if (byEnt instanceof Player) {
                GameSystem.getInstance().getDamageLogger().logDamage((Player) ent, (Player) byEnt);
            } else if (byEnt instanceof Arrow) {
                Arrow arrow = (Arrow) byEnt;

                if (arrow.getShooter() instanceof Player) {
                    GameSystem.getInstance().getDamageLogger().logDamage((Player) ent, (Player) arrow.getShooter());
                }
            }
        }
    }

}
