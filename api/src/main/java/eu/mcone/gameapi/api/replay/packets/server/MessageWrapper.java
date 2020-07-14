package eu.mcone.gameapi.api.replay.packets.server;

import eu.mcone.coresystem.api.bukkit.util.Messenger;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator
public class MessageWrapper {

    private final String message;
    private final Messenger.Broadcast.BroadcastMessageTyp typ;
    private final long timestamp;

    public MessageWrapper(final Messenger.Broadcast message) {
        this.message = message.getMessage();
        this.typ = message.getMessageTyp();
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public MessageWrapper(final String message, final Messenger.Broadcast.BroadcastMessageTyp typ) {
        this.message = message;
        this.typ = typ;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    @BsonCreator
    public MessageWrapper(@BsonProperty("message") final String message, @BsonProperty("typ") Messenger.Broadcast.BroadcastMessageTyp typ, @BsonProperty("timestamp") long timestamp) {
        this.message = message;
        this.typ = typ;
        this.timestamp = timestamp;
    }
}
