package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EntityTntExplodePacketWrapper extends EntityLocationWrapperTemplate {

    private List<SerializableBlock> destroy;

    public EntityTntExplodePacketWrapper(Location location, List<Block> destroy) {
        super(PacketType.SERVER, EntityAction.DESTROY, location);
        this.destroy = new ArrayList<>();

        for (Block block : destroy) {
            this.destroy.add(new SerializableBlock(block));
        }
    }
}
