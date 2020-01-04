package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.kit.GameKitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class KitSortListener implements Listener {

    private final GameKitManager manager;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();

            if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && e.getRawSlot() < inv.getSize() && e.getCurrentItem() != null) {
                if (inv.getTitle().endsWith(" §8| §7Kit sortieren") && e.getRawSlot() < 9) {
                    e.setCancelled(false);
                }
            }
        }
    }

}
