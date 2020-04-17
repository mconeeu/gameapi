package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.util.Messenger;
import lombok.Getter;

@Getter
public class ServerBroadcastMessagePacketWrapper extends PacketWrapper {

    private final Messenger.Broadcast message;
    private final long timestamp;

    public ServerBroadcastMessagePacketWrapper(final Messenger.Broadcast message) {
        super(PacketType.SERVER);

        this.message = message;
        this.timestamp = System.currentTimeMillis() / 1000;
    }
}
