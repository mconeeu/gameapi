package eu.mcone.gameapi.api.replay.packets.server;

import eu.mcone.coresystem.api.bukkit.broadcast.Broadcast;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@BsonDiscriminator
public class MessageWrapper {

    private Broadcast broadcast;
    private long timestamp;

    public MessageWrapper(Broadcast broadcast) {
        this.broadcast = broadcast;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

}
