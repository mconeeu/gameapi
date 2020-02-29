package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.replay.utils.Replay;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryContentInventory extends CoreInventory {

    public InventoryContentInventory(Player player, Replay replay) {
        super(replay.getPlayer().getData().getDisplayName(), player, InventorySlot.ROW_5);

        inventory.clear();

        int slot = 0;
        for (ItemStack itemStack : replay.getPlayer().getInventoryContent()) {
            setItem(slot, itemStack);
            slot++;
        }

        setItem(InventorySlot.ROW_5_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§cZurück").create(), e -> new ReplayPlayerInteractInventory(replay, player));
        openInventory();
    }
}
