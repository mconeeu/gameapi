package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;

@Getter
public class EntityDestroyPacketContainer extends PacketContainer {
    private String uuid;

    public EntityDestroyPacketContainer(String uuid) {
        super(PacketTyp.WORLD, EntityAction.DESTROY);
        this.uuid = uuid;
    }
}
