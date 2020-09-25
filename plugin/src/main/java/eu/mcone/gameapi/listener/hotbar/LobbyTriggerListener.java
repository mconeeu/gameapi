package eu.mcone.gameapi.listener.hotbar;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyTriggerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack i = e.getItem();
            if ((i == null) || (!i.hasItemMeta()) || (!i.getItemMeta().hasDisplayName())) {
                return;
            }

            if (i.equals(GamePlugin.getGamePlugin().getBackpackManager().getBackPackItem())) {
                e.setCancelled(true);
                GamePlugin.getGamePlugin().getBackpackManager().openBackpackInventory(DefaultCategory.GADGET.name(), p);
            } else if (i.equals(LobbyGameState.QUIT_ITEM)) {
                p.performCommand("hub");
            } else if (i.equals(GamePlugin.getGamePlugin().getTeamManager().getKitsChooseItem())) {
                GamePlugin.getGamePlugin().getTeamManager().openTeamInventory(p);
            }
        }
    }
}
