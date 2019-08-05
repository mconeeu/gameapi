package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.game.manager.kit.sorting.SortKitsInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void on(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null && e.getRawSlot() < inventory.getSize()) {
            if (e.getCurrentItem() != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                Kit currentKit = SortKitsInventory.getSorting().get(player);
                if (currentKit != null) {
                    if (inventory.getTitle().equalsIgnoreCase("§8» " + currentKit.getDisplayName())) {
                        if (e.getRawSlot() < inventory.getSize()) {
                            e.setCancelled(false);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();

        if (SortKitsInventory.getSorting().containsKey(player)) {
            Kit kit = SortKitsInventory.getSorting().get(player);
            if (inventory.getTitle().equalsIgnoreCase("§8» " + kit.getDisplayName())) {
                GamePlayer knockITPlayer = GameTemplate.getInstance().getGamePlayer(player.getUniqueId());

                HashMap<String, Double> sortedItems = new HashMap<>();
                int slot = 0;
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack != null) {
                        if (itemStack.getType() != null && itemStack.getType() != Material.AIR) {
                            sortedItems.put(Integer.toString(slot), kit.getKitItem(itemStack).getKitItemID());
                        }
                    }

                    slot++;
                }

                knockITPlayer.modifyInventory(kit, sortedItems);
                SortKitsInventory.getSorting().remove(player);
                new SortKitsInventory(player);

                GameTemplate.getInstance().getMessager().send(player, "§2Du hast die items für das Kit §f" + kit.getDisplayName() + " §2erfolgreich gesetzt!");
            }
        }
    }
}
