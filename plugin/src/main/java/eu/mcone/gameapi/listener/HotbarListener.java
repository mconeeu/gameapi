package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.*;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarListener implements Listener {

    @EventHandler
    public void onJoin(GamePlayerLoadedEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER) && GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
            if (GamePlugin.getGamePlugin().hasOption(Option.HOTBAR_SET_ITEMS)) {
                CorePlayer cp = e.getCorePlayer();
                Player player = cp.bukkit();
                BackpackManager backpackManager = GamePlugin.getGamePlugin().getBackpackManager();

                player.getInventory().setItem(0, HotbarItem.CHOOSE_TEAM);
                player.getInventory().setItem(1, HotbarItem.BACKPACK);
                backpackManager.setCurrentBackpackItem(e.getPlayer());

                if (GamePlugin.getGamePlugin().hasModule(Module.KIT_MANAGER)
                        && GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME)
                ) {
                    player.getInventory().setItem(7, HotbarItem.CHOOSE_KIT);
                }
                player.getInventory().setItem(8, HotbarItem.QUIT);

                /* Set rank boots */
                if (backpackManager.isUseRankBoots()) {
                    backpackManager.setRankBoots(e.getPlayer().bukkit());
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack i = e.getItem();
            if ((i == null) || (!i.hasItemMeta()) || (!i.getItemMeta().hasDisplayName())) {
                return;
            }

            if (i.equals(HotbarItem.BACKPACK)) {
                e.setCancelled(true);
                GamePlugin.getGamePlugin().getBackpackManager().openBackpackInventory(DefaultCategory.GADGET.name(), p);
            } else if (i.equals(HotbarItem.QUIT)) {
                e.setCancelled(true);
                CoreSystem.getInstance().getChannelHandler().createSetRequest(p, "CMD", "lobby");
            } else if (i.equals(HotbarItem.CHOOSE_TEAM)) {
                e.setCancelled(true);

                if (!GamePlugin.getGamePlugin().getTeamManager().isTeamsFinallySet()) {
                    GamePlugin.getGamePlugin().getTeamManager().openTeamInventory(p);
                } else {
                    GameAPI.getInstance().getMessenger().send(p, "§4Du kannst dein Team nicht mehr ändern!");
                }
            }
        }
    }
}
