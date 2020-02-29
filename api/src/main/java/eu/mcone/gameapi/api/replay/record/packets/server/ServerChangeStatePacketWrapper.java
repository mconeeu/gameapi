package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator
@Getter
public class ServerChangeStatePacketWrapper extends PacketWrapper {

    private final String oldState;
    private final String newState;

    @BsonCreator
    public ServerChangeStatePacketWrapper(@BsonProperty("oldState") final String oldState, @BsonProperty("newState") final String newState) {
        super(PacketType.SERVER, ServerAction.STATE_CHANGE);
        this.oldState = oldState;
        this.newState = newState;
    }
}
