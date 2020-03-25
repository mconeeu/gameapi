package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;

public class EntityDeathEventPacketWrapper extends PacketWrapper {

    public EntityDeathEventPacketWrapper() {
        super(PacketType.ENTITY, EntityAction.DESTROY);
    }
}
