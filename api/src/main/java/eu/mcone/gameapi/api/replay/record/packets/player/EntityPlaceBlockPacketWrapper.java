package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityPlaceBlockPacketWrapper extends EntityLocationWrapperTemplate {

    private String material;

    public EntityPlaceBlockPacketWrapper(final Block block) {
        super(PacketType.ENTITY, EntityAction.PLACE_BLOCK, block.getLocation());
        this.material = block.getType().toString();
    }

    public ItemStack constructItemStack() {
        return new ItemBuilder(Material.valueOf(material), 1).create();
    }
}
