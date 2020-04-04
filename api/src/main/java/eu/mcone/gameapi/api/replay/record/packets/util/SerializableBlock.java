package eu.mcone.gameapi.api.replay.record.packets.util;

import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.Serializable;

@Getter
@BsonDiscriminator
public class SerializableBlock implements Serializable {
    private double x;
    private double y;
    private double z;
    private String worldName;

    private String material;
    private int subID;

    public SerializableBlock(Block block) {
        this.material = block.getType().toString();
        this.subID = block.getData();
        this.x = block.getLocation().getX();
        this.y = block.getLocation().getY();
        this.z = block.getLocation().getZ();
        this.worldName = block.getLocation().getWorld().getName();
    }

    @BsonCreator
    public SerializableBlock(@BsonProperty("x") double x, @BsonProperty("y") double y, @BsonProperty("z") double z,
                             @BsonProperty("worldName") String worldName, @BsonProperty("material") String material, @BsonProperty("subID") int subID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
        this.material = material;
        this.subID = subID;
    }

    public Material getMaterial() {
        return Material.valueOf(material);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z, 0, 0);
    }
}
