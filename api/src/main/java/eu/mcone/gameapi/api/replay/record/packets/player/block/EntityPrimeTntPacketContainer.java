package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import org.bukkit.Location;

public class EntityPrimeTntPacketContainer extends EntityLocationTContainer {

    public EntityPrimeTntPacketContainer(Location location) {
        super(PacketTyp.ENTITY, EntityAction.INTERACT, location);
    }
}
