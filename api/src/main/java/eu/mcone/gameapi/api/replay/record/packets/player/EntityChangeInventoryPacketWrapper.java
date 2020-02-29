package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.gameapi.api.replay.record.packets.player.template.ItemStackArrayPacketTemplate;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@BsonDiscriminator
public class EntityChangeInventoryPacketWrapper extends ItemStackArrayPacketTemplate {

    public EntityChangeInventoryPacketWrapper(ItemStack[] itemStacks) {
        super(EntityAction.CHANGE_INVENTORY, itemStacks);
    }

    @BsonCreator
    public EntityChangeInventoryPacketWrapper(@BsonProperty("packetType") PacketType packetType, @BsonProperty("entityAction") EntityAction entityAction, @BsonProperty("itemStacks") List<SerializableItemStack> itemStacks) {
        super(packetType, entityAction, itemStacks);
    }

    @BsonIgnore
    public Inventory constructInventory() {
        Inventory inventory = Bukkit.createInventory(null, InventorySlot.ROW_6, "§7Inventar");
        ItemStack[] items = constructItemStackArray();

        if (items.length > 0) {
            int slot = 0;
            for (ItemStack itemStack : constructItemStackArray()) {
                inventory.setItem(slot, itemStack);
                slot++;
            }
        } else {
            inventory.setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.BARRIER).displayName("§cKeine Items vorhanden!").create());
        }

        return inventory;
    }
}
