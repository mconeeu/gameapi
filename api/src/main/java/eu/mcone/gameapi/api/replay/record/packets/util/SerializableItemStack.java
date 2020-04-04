package eu.mcone.gameapi.api.replay.record.packets.util;

import eu.mcone.coresystem.api.bukkit.config.typeadapter.ItemStackTypeAdapterUtils;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Map;

@Getter
public class SerializableItemStack implements Serializable {

    private String material;
    private int subID;
    private int amount;
    private String enchantments;

    public SerializableItemStack(ItemStack itemStack) {
        if (itemStack.getType() != null) {
            material = itemStack.getType().toString();
            subID = itemStack.getTypeId();
            amount = itemStack.getAmount();
            enchantments = ItemStackTypeAdapterUtils.serializeEnchantments(itemStack.getEnchantments());
        }
    }

    public ItemStack constructItemStack() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(material), amount);
        if (enchantments != null) {
            Map<Enchantment, Integer> enchantmentMap = ItemStackTypeAdapterUtils.getEnchantments(enchantments);
            itemBuilder.enchantments(enchantmentMap);
        }

        return itemBuilder.create();
    }
}
