package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.coresystem.api.bukkit.util.Messager;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator
public class ServerBroadcastMessagePacketWrapper extends PacketWrapper {

    private final Messager.Broadcast message;
    private final long timestamp;

    public ServerBroadcastMessagePacketWrapper(final Messager.Broadcast message) {
        super(PacketType.SERVER, ServerAction.BROADCAST);

        this.message = message;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    @BsonCreator
    public ServerBroadcastMessagePacketWrapper(@BsonProperty("message") final Messager.Broadcast message, @BsonProperty("timestamp") final long timestamp) {
        super(PacketType.SERVER, ServerAction.BROADCAST);

        this.message = message;
        this.timestamp = timestamp;
    }
}
