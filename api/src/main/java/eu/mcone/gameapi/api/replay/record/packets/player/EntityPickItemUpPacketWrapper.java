package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.entity.Item;

@BsonDiscriminator
public class EntityPickItemUpPacketWrapper extends PacketWrapper {

    @Getter
    private String item;

    public EntityPickItemUpPacketWrapper(final Item item) {
        super(PacketType.ENTITY, EntityAction.PICK_ITEM_UP);
        this.item = item.getName();
    }

    @BsonCreator
    public EntityPickItemUpPacketWrapper(@BsonProperty("item") final String item) {
        super(PacketType.ENTITY, EntityAction.PICK_ITEM_UP);
        this.item = item;
    }
}
