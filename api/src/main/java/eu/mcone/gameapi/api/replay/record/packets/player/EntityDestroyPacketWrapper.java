package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator
public class EntityDestroyPacketWrapper extends PacketWrapper {
    private String uuid;

    @BsonCreator
    public EntityDestroyPacketWrapper(@BsonProperty("uuid") final String uuid) {
        super(PacketType.WORLD, EntityAction.DESTROY);
        this.uuid = uuid;
    }
}
