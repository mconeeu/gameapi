package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.game.inventory.SpectatorInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (GameTemplate.getInstance() != null) {
            if (e.getItem() != null) {
                if (e.getItem().equals(SpectatorInventory.NAVIGATOR)) {
                    new SpectatorInventory(e.getPlayer());
                }
            }
        }
    }
}
