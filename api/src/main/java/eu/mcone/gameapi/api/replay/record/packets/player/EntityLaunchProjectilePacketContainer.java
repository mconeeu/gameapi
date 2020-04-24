package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile;
import lombok.Getter;

@Getter
public class EntityLaunchProjectilePacketContainer extends PacketContainer {

    private String projectile;

    public EntityLaunchProjectilePacketContainer(EntityProjectile projectile) {
        super(PacketTyp.ENTITY, EntityAction.SHOOT_PROJECTILE);
        this.projectile = projectile.name();
    }
}
