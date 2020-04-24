package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class EntitySpawnPacketContainer extends EntityLocationTContainer {

    public EntitySpawnPacketContainer(Location location) {
        super(PacketTyp.WORLD, EntityAction.SPAWN, location);
    }
}
