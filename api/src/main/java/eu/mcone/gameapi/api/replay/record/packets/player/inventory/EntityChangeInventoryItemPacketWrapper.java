package eu.mcone.gameapi.api.replay.record.packets.player.inventory;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityChangeInventoryItemPacketWrapper extends PacketWrapper {

    private int slot;
    private SerializableItemStack serializableItemStack;

    public EntityChangeInventoryItemPacketWrapper(int slot, ItemStack itemStack) {
        super(PacketType.ENTITY, EntityAction.CHANGE_INVENTORY);
        this.slot = slot;
        this.serializableItemStack = new SerializableItemStack(itemStack);
    }

    public EntityChangeInventoryItemPacketWrapper(int slot, net.minecraft.server.v1_8_R3.ItemStack itemStack) {
        super(PacketType.ENTITY, EntityAction.CHANGE_INVENTORY);
        this.slot = slot;
        this.serializableItemStack = new SerializableItemStack(CraftItemStack.asBukkitCopy(itemStack));
    }
}
