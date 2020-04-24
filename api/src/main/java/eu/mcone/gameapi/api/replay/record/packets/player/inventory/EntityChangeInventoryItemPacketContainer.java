package eu.mcone.gameapi.api.replay.record.packets.player.inventory;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityChangeInventoryItemPacketContainer extends PacketContainer {

    private final int slot;
    private final SerializableItemStack serializableItemStack;

    public EntityChangeInventoryItemPacketContainer(int slot, ItemStack itemStack) {
        super(PacketTyp.ENTITY, EntityAction.CHANGE_INVENTORY);
        this.slot = slot;
        this.serializableItemStack = new SerializableItemStack(itemStack);
    }

    public EntityChangeInventoryItemPacketContainer(int slot, net.minecraft.server.v1_8_R3.ItemStack itemStack) {
        super(PacketTyp.ENTITY, EntityAction.CHANGE_INVENTORY);
        this.slot = slot;
        this.serializableItemStack = new SerializableItemStack(CraftItemStack.asBukkitCopy(itemStack));
    }
}
