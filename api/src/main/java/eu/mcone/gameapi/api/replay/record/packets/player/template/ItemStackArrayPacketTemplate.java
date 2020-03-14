package eu.mcone.gameapi.api.replay.record.packets.player.template;

import eu.mcone.coresystem.api.bukkit.config.typeadapter.ItemStackTypeAdapterUtils;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ItemStackArrayPacketTemplate extends PacketWrapper {

    private List<SerializableItemStack> itemStacks;

    public ItemStackArrayPacketTemplate(EntityAction entityAction, ItemStack[] itemStacks) {
        super(PacketType.ENTITY, entityAction);
        this.itemStacks = new ArrayList<>();

        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.getType().equals(Material.AIR)) {
                this.itemStacks.add(new SerializableItemStack(itemStack));
            }
        }
    }

    public ItemStack[] constructItemStackArray() {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (SerializableItemStack serializableItemStack : this.itemStacks) {
            ItemStack itemStack = new ItemStack(Material.valueOf(serializableItemStack.getMaterial()));
            itemStack.setAmount(serializableItemStack.getAmount());

            Map<Enchantment, Integer> enchantmentMap = ItemStackTypeAdapterUtils.getEnchantments(serializableItemStack.getEnchantments());
            for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
                itemStack.addEnchantment(entry.getKey(), entry.getValue());
            }

            itemStacks.add(itemStack);
        }

        return itemStacks.toArray(new ItemStack[0]);
    }
}
