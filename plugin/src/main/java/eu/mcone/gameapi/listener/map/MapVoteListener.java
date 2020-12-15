/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.map;

import eu.mcone.gameapi.inventory.MapInventory;
import eu.mcone.gameapi.map.GameMapVotingHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class MapVoteListener implements Listener {

    private final GameMapVotingHandler mapVotingHandler;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            if (e.getInventory().getTitle().equals(MapInventory.TITLE)) {
                mapVotingHandler.closedInventory((Player) e.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(PlayerQuitEvent e) {
        mapVotingHandler.closedInventory(e.getPlayer());
    }

}
