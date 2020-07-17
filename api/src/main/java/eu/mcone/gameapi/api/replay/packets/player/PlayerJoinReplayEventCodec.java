package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplayEvent;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerJoinReplayEventCodec extends Codec<PlayerJoinReplayEvent, PlayerRunner> {

    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public PlayerJoinReplayEventCodec() {
        super("Join", PlayerJoinReplayEvent.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, PlayerJoinReplayEvent playerJoin) {
        this.world = player.getLocation().getWorld().getName();
        this.x = player.getLocation().getX();
        this.y = player.getLocation().getY();
        this.z = player.getLocation().getZ();
        this.yaw = player.getLocation().getYaw();
        pitch = player.getLocation().getPitch();

        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().getNpc().teleport(getLocation());
        runner.getPlayer().getNpc().togglePlayerVisibility(ListMode.WHITELIST, runner.getWatchers().toArray(new Player[0]));
        Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(runner.getPlayer().getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.START));
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(world);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        world = in.readUTF();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
        yaw = in.readFloat();
        pitch = in.readFloat();
    }

    private CoreLocation getLocation() {
        return new CoreLocation(world, x, y, z, yaw, pitch);
    }
}
