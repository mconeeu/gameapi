package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;

@Getter
public class EntityDestroyPacketWrapper extends PacketWrapper {
    private String uuid;

    public EntityDestroyPacketWrapper(String uuid) {
        super(PacketType.WORLD, EntityAction.DESTROY);
        this.uuid = uuid;
    }
}
