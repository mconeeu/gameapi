package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationWrapperTemplate;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityBreakBlockPacketWrapper extends EntityLocationWrapperTemplate {

    private String block;

    public EntityBreakBlockPacketWrapper(final Block block) {
        super(PacketType.ENTITY, EntityAction.BREAK_BLOCK, block.getLocation());
        this.block = block.getType().toString();
    }

    @BsonIgnore
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.valueOf(block), 1).create();
    }
}
