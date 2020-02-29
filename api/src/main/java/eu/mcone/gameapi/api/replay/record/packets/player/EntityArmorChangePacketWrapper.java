package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.gameapi.api.replay.record.packets.player.template.ItemStackArrayPacketTemplate;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@BsonDiscriminator
public class EntityArmorChangePacketWrapper extends ItemStackArrayPacketTemplate {
    public EntityArmorChangePacketWrapper(ItemStack[] itemStacks) {
        super(EntityAction.CHANGE_ARMOR, itemStacks);
    }

    @BsonCreator
    public EntityArmorChangePacketWrapper(@BsonProperty("packetType") PacketType packetType, @BsonProperty("entityAction") EntityAction entityAction, @BsonProperty("itemStacks") List<SerializableItemStack> itemStacks) {
        super(packetType, entityAction, itemStacks);
    }
}
