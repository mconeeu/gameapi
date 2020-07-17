package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class BlockBreakEventCodec extends Codec<BlockBreakEvent, PlayerRunner> {

    private double x;
    private double y;
    private double z;
    private String world;

    public BlockBreakEventCodec() {
        super("BlockBreak", BlockBreakEvent.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, BlockBreakEvent blockBreakEvent) {
        Block block = blockBreakEvent.getBlock();
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        world = block.getWorld().getName();
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (Player player : runner.getWatchers()) {
            player.sendBlockChange(getLocation().bukkit(), Material.AIR, (byte) 0);
        }
    }

    private CoreLocation getLocation() {
        return new CoreLocation(world, x, y, z, 0, 0);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(world);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        world = in.readUTF();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
