package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;

public class EntityDeathEventPacketContainer extends PacketContainer {

    public EntityDeathEventPacketContainer() {
        super(PacketTyp.ENTITY, EntityAction.DESTROY);
    }
}
