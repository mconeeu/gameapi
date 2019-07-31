package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.game.inventory.SpectatorInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class InventoryMoveItem implements Listener {

    @EventHandler
    public void on(InventoryMoveItemEvent e) {
        if (GameTemplate.getInstance() != null) {
            if (e.getItem().equals(SpectatorInventory.NAVIGATOR)) {
                e.setCancelled(true);
            }
        }
    }
}
