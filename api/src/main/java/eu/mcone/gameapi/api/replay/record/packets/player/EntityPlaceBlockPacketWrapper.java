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
public class EntityPlaceBlockPacketWrapper extends PacketWrapper {

    private Material material;
    private Location location;

    public EntityPlaceBlockPacketWrapper(final Block block) {
        super(PacketType.ENTITY, WorldAction.SET_BLOCK);

        this.material = block.getType();
        this.location = block.getLocation();
    }

    @BsonCreator
    public EntityPlaceBlockPacketWrapper(@BsonProperty("worldAction") final WorldAction worldAction, @BsonProperty("material") final Material material, @BsonProperty("location") final Location location) {
        super(PacketType.ENTITY, worldAction);

        this.material = material;
        this.location = location;
    }

    @BsonIgnore
    public ItemStack constructItemStack() {
        return new ItemBuilder(material, 1).create();
    }
}
