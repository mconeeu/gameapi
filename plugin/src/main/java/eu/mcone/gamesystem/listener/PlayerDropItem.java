package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.game.inventory.SpectatorInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void on(PlayerDropItemEvent e) {
        if (GameTemplate.getInstance() != null) {
            if (e.getItemDrop().getItemStack().equals(SpectatorInventory.NAVIGATOR)) {
                e.setCancelled(true);
            }
        }
    }
}
