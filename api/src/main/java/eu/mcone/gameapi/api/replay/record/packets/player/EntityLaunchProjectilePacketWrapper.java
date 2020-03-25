package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile;
import lombok.Getter;

@Getter
public class EntityLaunchProjectilePacketWrapper extends PacketWrapper {

    private String projectile;

    public EntityLaunchProjectilePacketWrapper(EntityProjectile projectile) {
        super(PacketType.ENTITY, EntityAction.SHOOT_PROJECTILE);
        this.projectile = projectile.name();
    }
}
