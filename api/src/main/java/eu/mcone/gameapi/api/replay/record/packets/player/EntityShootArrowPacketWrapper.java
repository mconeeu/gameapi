package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class EntityShootArrowPacketWrapper extends EntityLocationWrapperTemplate {

    public EntityShootArrowPacketWrapper(Location destination) {
        super(PacketType.ENTITY, EntityAction.SHOOT_PROJECTILE, destination);
    }

    public Vector getVector() {
        return new Vector(getX(), getY(), getZ());
    }
}
