package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.coresystem.api.bukkit.util.Messenger;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator
public class ServerBroadcastMessagePacketContainer extends PacketContainer {

    private final Messenger.Broadcast message;
    private final long timestamp;

    public ServerBroadcastMessagePacketContainer(final Messenger.Broadcast message) {
        super(PacketTyp.SERVER);

        this.message = message;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    @BsonCreator
    public ServerBroadcastMessagePacketContainer(@BsonProperty("message") final Messenger.Broadcast message, @BsonProperty("timestamp") long timestamp) {
        super(PacketTyp.SERVER);
        this.message = message;
        this.timestamp = timestamp;
    }
}
