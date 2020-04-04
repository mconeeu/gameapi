package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

@Getter
public class EntityBlockActionPacketWrapper extends PacketWrapper {
    private String action;

    public EntityBlockActionPacketWrapper(PacketPlayInBlockDig.EnumPlayerDigType type) {
        super(PacketType.ENTITY, EntityAction.BLOCK_ANIMATION);
        this.action = type.name();
    }
}
