package eu.mcone.gameapi.api.replay.record.packets.player.template;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import lombok.Getter;
import org.bukkit.block.Block;

@Getter
public class BlockPacketTContainer extends EntityLocationTContainer {

    public BlockPacketTContainer(EntityAction action, Block block) {
        super(PacketTyp.ENTITY, action, block.getLocation());
    }

    public BlockPacketTContainer(EntityAction action, int x, int y, int z, String world) {
        super(PacketTyp.ENTITY, action, x, y, z, 0, 0, world);
    }

    public BlockPacketTContainer(EntityAction action, int x, int y, int z) {
        super(PacketTyp.ENTITY, action, x, y, z, 0, 0, "");
    }
}
