package eu.mcone.gameapi.listener.backpack;

import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
public class BackpackListener implements Listener {

    private final GameBackpackManager manager;

    @EventHandler
    public void on(InventoryCloseEvent e) {
        /*if (e.getPlayer() instanceof Player) {
            if (e.getInventory().getTitle().equals("§8» §3§lWähle aus") || e.getInventory().getTitle().equals("§8» §e§lTrade")) {
                manager.cancelTraid((Player) e.getPlayer());
            }
        }*/
    }

}
