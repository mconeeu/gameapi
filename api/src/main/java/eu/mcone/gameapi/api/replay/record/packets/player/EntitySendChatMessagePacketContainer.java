package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Getter
@BsonDiscriminator
public class EntitySendChatMessagePacketContainer extends PacketContainer {

    private String message;
    private long timestamp;

    public EntitySendChatMessagePacketContainer(final String msg) {
        super(PacketTyp.ENTITY, EntityAction.CHAT);
        this.timestamp = System.currentTimeMillis() / 1000;
        this.message = msg;
    }
}
