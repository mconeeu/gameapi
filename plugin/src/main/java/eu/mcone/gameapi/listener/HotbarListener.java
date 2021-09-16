/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.HotbarItem;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(GamePlayerLoadedEvent e) {
        if (GamePlugin.isGamePluginInitialized()
                && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)
                && GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState
                && GamePlugin.getGamePlugin().hasOption(Option.HOTBAR_SET_ITEMS)
        ) {
            CorePlayer cp = e.getCorePlayer();
            Player player = cp.bukkit();
            BackpackManager backpackManager = GamePlugin.getGamePlugin().getBackpackManager();

            player.getInventory().setItem(0, HotbarItem.CHOOSE_TEAM);
            player.getInventory().setItem(1, HotbarItem.BACKPACK);
            backpackManager.setCurrentBackpackItem(e.getPlayer(), false);

            if (GamePlugin.getGamePlugin().hasModule(Module.KIT_MANAGER)
                    && GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME)
            ) {
                player.getInventory().setItem(7, HotbarItem.CHOOSE_KIT);
            }
            player.getInventory().setItem(8, HotbarItem.QUIT);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (GamePlugin.isGamePluginInitialized() && GamePlugin.getGamePlugin().hasOption(Option.HOTBAR_SET_ITEMS) && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack i = e.getItem();
            if ((i == null) || (!i.hasItemMeta()) || (!i.getItemMeta().hasDisplayName())) {
                return;
            }

            if (i.equals(HotbarItem.BACKPACK)) {
                e.setCancelled(true);
                GamePlugin.getGamePlugin().getBackpackManager().openBackpackInventory(p);
            } else if (i.equals(HotbarItem.QUIT)) {
                e.setCancelled(true);
                CoreSystem.getInstance().getChannelHandler().createSetRequest(p, "CMD", "lobby");
            } else if (i.equals(HotbarItem.CHOOSE_TEAM)) {
                e.setCancelled(true);

                if (!GamePlugin.getGamePlugin().getTeamManager().isTeamsFinallySet()) {
                    GamePlugin.getGamePlugin().getTeamManager().openTeamInventory(p);
                } else {
                    Msg.send(p, "§4Du kannst dein Team nicht mehr ändern!");
                }
            }
        }
    }
}
