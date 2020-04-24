package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.gameapi.api.replay.record.packets.player.template.BlockPacketTContainer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.block.Block;

@Getter
public class EntityBlockBreakPacketContainer extends BlockPacketTContainer {

    public EntityBlockBreakPacketContainer(final Block block) {
        super(EntityAction.BREAK_BLOCK, block);
    }

    public EntityBlockBreakPacketContainer(final BlockPosition position, final String world) {
        super(EntityAction.BREAK_BLOCK, position.getX(), position.getY(), position.getZ(), world);
    }

    public EntityBlockBreakPacketContainer(int x, int y, int z, String world) {
        super(EntityAction.BREAK_BLOCK, x, y, z, world);
    }
}
