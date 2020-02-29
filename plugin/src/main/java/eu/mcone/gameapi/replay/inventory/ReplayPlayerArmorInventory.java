package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.enums.EquipmentPosition;
import eu.mcone.gameapi.replay.utils.Replay;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ReplayPlayerArmorInventory extends CoreInventory {

    public ReplayPlayerArmorInventory(Replay replay, Player player) {
        super(replay.getPlayer().getData().getDisplayName(), player, InventorySlot.ROW_2, InventoryOption.FILL_EMPTY_SLOTS);

        for (Map.Entry<EquipmentPosition, ItemStack> entry : ((PlayerNpcData) replay.getNpc().getEntityData()).getEquipment().entrySet()) {
            setItem(entry.getKey().getId(), entry.getValue());
        }

        setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§7Zurück").create(), e -> new ReplayPlayerInteractInventory(replay, player));
        openInventory();
    }
}
