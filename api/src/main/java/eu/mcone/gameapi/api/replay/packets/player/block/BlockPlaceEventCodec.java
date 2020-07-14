package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class BlockPlaceEventCodec extends Codec<BlockPlaceEvent, PlayerRunner> {

    private String material;
    private byte subID;

    private double x;
    private double y;
    private double z;
    private String world;

    public BlockPlaceEventCodec() {
        super("BlockPlace");
    }

    @BsonIgnore
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.valueOf(material), 1, subID).create();
    }

    @Override
    public Object[] decode(Player player, BlockPlaceEvent blockPlaceEvent) {
        Block block = blockPlaceEvent.getBlock();

        material = block.getType().toString();
        subID = block.getData();

        x = block.getLocation().getX();
        y = block.getLocation().getY();
        z = block.getLocation().getZ();
        world = block.getLocation().getWorld().getName();
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (Player player : runner.getWatchers()) {
            player.sendBlockChange(getLocation().bukkit(), Material.valueOf(material), subID);
        }
    }

    private CoreLocation getLocation() {
        return new CoreLocation(world, x, y, z, 0, 0);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(material);
        out.writeByte(subID);

        out.writeUTF(world);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        material = in.readUTF();
        subID = in.readByte();

        world = in.readUTF();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
