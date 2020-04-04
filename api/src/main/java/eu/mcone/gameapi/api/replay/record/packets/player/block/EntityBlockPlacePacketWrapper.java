package eu.mcone.gameapi.api.replay.record.packets.player.block;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.gameapi.api.replay.record.packets.player.template.BlockPacketTemplate;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityBlockPlacePacketWrapper extends BlockPacketTemplate {

    private final String material;
    private final byte subID;

    public EntityBlockPlacePacketWrapper(final Block block) {
        super(EntityAction.PLACE_BLOCK, block);
        this.material = block.getType().toString();
        this.subID = block.getData();
    }

    public EntityBlockPlacePacketWrapper(final ItemStack itemStack, BlockPosition position, String world) {
        super(EntityAction.PLACE_BLOCK, position.getX(), position.getY(), position.getZ(), world);
        this.material = itemStack.getType().toString();
        this.subID = itemStack.getData().getData();
    }

    @BsonIgnore
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.valueOf(material), 1, subID).create();
    }
}
