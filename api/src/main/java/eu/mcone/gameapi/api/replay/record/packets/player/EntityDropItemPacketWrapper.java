package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityItemPacketWrapperTemplate;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@BsonDiscriminator
public class EntityDropItemPacketWrapper extends EntityItemPacketWrapperTemplate {

    public EntityDropItemPacketWrapper(final ItemStack item) {
        super(EntityAction.DROP_ITEM, item);
    }

    @BsonCreator
    public EntityDropItemPacketWrapper(@BsonProperty("material") final Material material, @BsonProperty("amount") final int amount, @BsonProperty("enchantments") final String enchantments) {
        super(EntityAction.DROP_ITEM, material, amount, enchantments);
    }
}
