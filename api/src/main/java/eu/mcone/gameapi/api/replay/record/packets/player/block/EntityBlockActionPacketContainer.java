package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

@Getter
public class EntityBlockActionPacketContainer extends PacketContainer {
    private String action;

    public EntityBlockActionPacketContainer(PacketPlayInBlockDig.EnumPlayerDigType type) {
        super(PacketTyp.ENTITY, EntityAction.BLOCK_ANIMATION);
        this.action = type.name();
    }
}
