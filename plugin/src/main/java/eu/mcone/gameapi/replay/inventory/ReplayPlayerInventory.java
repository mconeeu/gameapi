package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class ReplayPlayerInventory extends CoreInventory {

    public ReplayPlayerInventory(Player player, ReplayPlayer replay, Map<Integer, SerializableItemStack> items) {
        super(replay.getData().getDisplayName(), player, InventorySlot.ROW_5);

        if (!items.isEmpty()) {
            for (Map.Entry<Integer, SerializableItemStack> entry : items.entrySet()) {
                inventory.setItem(entry.getKey(), entry.getValue().constructItemStack());
            }
        } else {
            inventory.setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.BARRIER).displayName("§cKeine Items vorhanden!").create());
        }

        setItem(InventorySlot.ROW_5_SLOT_1, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_2, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_3, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_4, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_5, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_6, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_7, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_8, CoreInventory.PLACEHOLDER_ITEM);
        setItem(InventorySlot.ROW_5_SLOT_9, CoreInventory.BACK_ITEM, e -> new ReplayPlayerInteractInventory(replay, player));
        openInventory();
    }
}
