/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
public class BackpackListener implements Listener {

    private final GameBackpackManager manager;

    @EventHandler
    public void on(GamePlayerLoadedEvent e) {
        if (GamePlugin.getGamePlugin().hasOption(Option.BACKPACK_MANAGER_AUTO_SET_RANK_BOOTS)) {
            manager.setRankBoots(e.getPlayer().bukkit());
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent e) {
        /*if (e.getPlayer() instanceof Player) {
            if (e.getInventory().getTitle().equals("§8» §3§lWähle aus") || e.getInventory().getTitle().equals("§8» §e§lTrade")) {
                manager.cancelTraid((Player) e.getPlayer());
            }
        }*/
    }

}
