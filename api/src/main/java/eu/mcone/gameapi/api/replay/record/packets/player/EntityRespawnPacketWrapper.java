package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import org.bukkit.Location;

public class EntityRespawnPacketWrapper extends EntityLocationWrapperTemplate {

    public EntityRespawnPacketWrapper(Location respawn) {
        super(PacketType.ENTITY, EntityAction.RESPAWN, respawn);
    }
}
