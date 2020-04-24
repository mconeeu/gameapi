package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class EntityShootArrowPacketContainer extends EntityLocationTContainer {

    public EntityShootArrowPacketContainer(Location destination) {
        super(PacketTyp.ENTITY, EntityAction.SHOOT_PROJECTILE, destination);
    }

    public Vector getVector() {
        return new Vector(getX(), getY(), getZ());
    }
}
