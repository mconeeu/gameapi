package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import org.bukkit.Location;

public class EntityRespawnPacketContainer extends EntityLocationTContainer {

    public EntityRespawnPacketContainer(Location respawn) {
        super(PacketTyp.ENTITY, EntityAction.RESPAWN, respawn);
    }
}
