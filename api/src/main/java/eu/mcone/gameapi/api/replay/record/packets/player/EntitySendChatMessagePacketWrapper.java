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
public class EntitySendChatMessagePacketWrapper extends PacketWrapper {

    private ChatMessage chatMessage;

    public EntitySendChatMessagePacketWrapper(final String msg) {
        super(PacketType.ENTITY, EntityAction.CHAT);

        this.chatMessage = new ChatMessage(msg, System.currentTimeMillis() / 1000);
    }

    @BsonCreator
    public EntitySendChatMessagePacketWrapper(@BsonProperty("msg") final String msg, @BsonProperty("timestamp") final long timestamp) {
        super(PacketType.ENTITY, EntityAction.CHAT);

        this.chatMessage = new ChatMessage(msg, timestamp);
    }

    @Getter
    public class ChatMessage {
        private String message;
        private long timestamp;

        @BsonCreator
        public ChatMessage(@BsonProperty("message") final String message, @BsonProperty("timestamp") final long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
    }
}
