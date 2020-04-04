package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import org.bukkit.Location;

public class EntityPrimeTntPacketWrapper extends EntityLocationWrapperTemplate {

    public EntityPrimeTntPacketWrapper(Location location) {
        super(PacketType.ENTITY, EntityAction.INTERACT, location);
    }
}
