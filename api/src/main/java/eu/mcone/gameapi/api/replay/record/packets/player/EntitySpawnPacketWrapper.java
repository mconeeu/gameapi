package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class EntitySpawnPacketWrapper extends EntityLocationWrapperTemplate {

    public EntitySpawnPacketWrapper(Location location) {
        super(PacketType.WORLD, EntityAction.SPAWN, location);
    }
}
