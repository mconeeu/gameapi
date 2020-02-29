package eu.mcone.gameapi.api.replay.record.packets.player.template;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@BsonDiscriminator
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

    @BsonCreator
    public ItemStackArrayPacketTemplate(@BsonProperty("packetType") PacketType packetType, @BsonProperty("entityAction") EntityAction entityAction, @BsonProperty("itemStacks") List<SerializableItemStack> itemStacks) {
        super(packetType, entityAction);
        this.itemStacks = itemStacks;
    }

    @BsonIgnore
    public ItemStack[] constructItemStackArray() {
        List<ItemStack> itemStacks = new ArrayList<>();

        for (SerializableItemStack serializableItemStack : this.itemStacks) {
            ItemStack itemStack = new ItemStack(Material.valueOf(serializableItemStack.getMaterial()));
            itemStack.setAmount(serializableItemStack.getAmount());

            for (Map.Entry<String, String> entry : serializableItemStack.getEnchantments().entrySet()) {
                itemStack.addEnchantment(Enchantment.getByName(entry.getKey()), Integer.valueOf(entry.getValue()));
            }

            itemStacks.add(itemStack);
        }

        return itemStacks.toArray(new ItemStack[0]);
    }
}
