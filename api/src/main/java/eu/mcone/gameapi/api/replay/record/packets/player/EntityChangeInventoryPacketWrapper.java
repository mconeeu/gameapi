package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class EntityChangeInventoryPacketWrapper extends PacketWrapper {

    private Map<Integer, SerializableItemStack> items;

    public EntityChangeInventoryPacketWrapper(Map<Integer, SerializableItemStack> items) {
        super(EntityAction.CHANGE_INVENTORY);
        this.items = items;
    }

    public ItemStack[] getContent() {
        List<ItemStack> array = new ArrayList<>();

        for (SerializableItemStack items : items.values()) {
            array.add(items.constructItemStack());
        }

        return array.toArray(new ItemStack[0]);
    }

    public Inventory constructInventory(final String invName) {
        Inventory inventory = Bukkit.createInventory(null, InventorySlot.ROW_5, invName);

        if (!this.items.isEmpty()) {
            for (Map.Entry<Integer, SerializableItemStack> entry : this.items.entrySet()) {
                inventory.setItem(entry.getKey(), entry.getValue().constructItemStack());
            }
        } else {
            inventory.setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.BARRIER).displayName("§cKeine Items vorhanden!").create());
        }

        inventory.setItem(InventorySlot.ROW_5_SLOT_1, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_2, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_3, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_4, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_5, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_6, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_7, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_8, CoreInventory.PLACEHOLDER_ITEM);
        inventory.setItem(InventorySlot.ROW_5_SLOT_9, CoreInventory.BACK_ITEM);

        return inventory;
    }
}
