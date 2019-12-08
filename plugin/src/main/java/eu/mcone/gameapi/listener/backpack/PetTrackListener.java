/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack;

import eu.mcone.gameapi.backpack.handler.GamePetHandler;
import eu.mcone.gameapi.inventory.backpack.AnimalInteractInventory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public class PetTrackListener implements Listener {

    private final GamePetHandler handler;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (handler.hasPet(p)) {
            handler.followPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        if (handler.hasPet(p)) {
            Entity entity = handler.getEntity(p);

            if (e.getRightClicked().equals(entity)) {
                new AnimalInteractInventory(handler, p, entity);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntityType().equals(EntityType.WITHER_SKULL)) {
            e.setCancelled(true);
        }
    }

}
