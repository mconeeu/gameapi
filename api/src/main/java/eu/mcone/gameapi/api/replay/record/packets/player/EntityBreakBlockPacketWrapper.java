package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.WorldAction;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@Getter
@BsonDiscriminator
public class EntityBreakBlockPacketWrapper extends PacketWrapper {

    private Material block;
    private Location location;

    public EntityBreakBlockPacketWrapper(final Block block) {
        super(PacketType.ENTITY, WorldAction.BREAK_BLOCK);

        this.block = block.getType();
        this.location = block.getLocation();
    }

    @BsonCreator
    public EntityBreakBlockPacketWrapper(@BsonProperty("worldAction") final WorldAction worldAction, @BsonProperty("material") final Material material, @BsonProperty("location") final Location location) {
        super(PacketType.ENTITY, worldAction);

        this.block = material;
        this.location = location;
    }

    @BsonIgnore
    public ItemStack getItemStack() {
        return new ItemBuilder(block, 1).create();
    }
}
