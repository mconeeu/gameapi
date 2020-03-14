package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Getter
@BsonDiscriminator
public class EntitySendChatMessagePacketWrapper extends PacketWrapper {

    private String message;
    private long timestamp;

    public EntitySendChatMessagePacketWrapper(final String msg) {
        super(PacketType.ENTITY, EntityAction.CHAT);
        this.timestamp = System.currentTimeMillis() / 1000;
        this.message = msg;
    }
}
