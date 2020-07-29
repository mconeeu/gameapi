package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.*;

@Getter
public class BlockBreakEventCodec extends Codec<BlockBreakEvent, PlayerRunner> {

    private double x;
    private double y;
    private double z;
    private String world;

    public BlockBreakEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, BlockBreakEvent blockBreakEvent) {
        Block block = blockBreakEvent.getBlock();
        x = block.getX();
        y = block.getY();
        z = block.getZ();
        world = block.getWorld().getName();
        return new Object[]{blockBreakEvent.getPlayer()};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (Player player : runner.getWatchers()) {
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                player.sendBlockChange(getLocation().bukkit(), Material.AIR, (byte) 0);
            }
        }
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeUTF(world);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException {
        world = in.readUTF();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }

    private CoreLocation getLocation() {
        return new CoreLocation(world, x, y, z, 0, 0);
    }
}
