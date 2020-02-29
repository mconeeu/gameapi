package eu.mcone.gameapi.api.replay.record.packets.util;

import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@BsonDiscriminator
@Getter
public class SerializableItemStack {

    private String material;
    private int amount;
    private Map<String, String> enchantments;

    public SerializableItemStack(ItemStack itemStack) {
        if (itemStack.getType() != null) {
            material = itemStack.getType().toString();
            amount = itemStack.getAmount();
            enchantments = new HashMap<>();

            if (itemStack.getEnchantments().size() > 0) {
                for (Map.Entry<Enchantment, Integer> enchantments : itemStack.getEnchantments().entrySet()) {
                    this.enchantments.put(enchantments.getKey().getName(), enchantments.getValue().toString());
                }
            }
        }
    }

    @BsonCreator
    public SerializableItemStack(@BsonProperty("material") String material, @BsonProperty("amount") int amount, @BsonProperty("enchantments") Map<String, String> enchantments) {
        this.material = material;
        this.amount = amount;
        this.enchantments = enchantments;
    }

    public ItemStack constructItemStack() {
        ItemStack itemStack = new ItemStack(Material.valueOf(material));
        itemStack.setAmount(amount);

        for (Map.Entry<String, String> enchantments : enchantments.entrySet()) {
            itemStack.addEnchantment(Enchantment.getByName(enchantments.getKey()), Integer.valueOf(enchantments.getValue()));
        }

        return itemStack;
    }
}
