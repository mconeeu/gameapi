package eu.mcone.gameapi.api.replay.record.packets.player.template;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import lombok.Getter;
import org.bukkit.block.Block;

@Getter
public class BlockPacketTemplate extends EntityLocationWrapperTemplate {

    public BlockPacketTemplate(EntityAction action, Block block) {
        super(PacketType.ENTITY, action, block.getLocation());
    }

    public BlockPacketTemplate(EntityAction action, int x, int y, int z, String world) {
        super(PacketType.ENTITY, action, x, y, z, 0, 0, world);
    }

    public BlockPacketTemplate(EntityAction action, int x, int y, int z) {
        super(PacketType.ENTITY, action, x, y, z, 0, 0, "");
    }
}
