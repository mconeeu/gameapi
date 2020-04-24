package eu.mcone.gameapi.api.replay.record.packets.server;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableBlock;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EntityTntExplodePacketContainer extends EntityLocationTContainer {

    private List<SerializableBlock> destroy;

    public EntityTntExplodePacketContainer(Location location, List<Block> destroy) {
        super(PacketTyp.SERVER, EntityAction.DESTROY, location);
        this.destroy = new ArrayList<>();

        for (Block block : destroy) {
            this.destroy.add(new SerializableBlock(block));
        }
    }
}
