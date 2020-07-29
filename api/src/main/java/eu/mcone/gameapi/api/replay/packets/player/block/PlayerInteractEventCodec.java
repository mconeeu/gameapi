package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.npc.enums.NpcAnimation;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.*;

public class PlayerInteractEventCodec extends Codec<PlayerInteractEvent, PlayerRunner> {

    private eu.mcone.gameapi.api.replay.packets.player.block.Action action;
    private double x;
    private double y;
    private double z;

    public PlayerInteractEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PlayerInteractEvent interactEvent) {
        if (interactEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && interactEvent.getClickedBlock().getType().equals(org.bukkit.Material.TNT)
                && interactEvent.getPlayer().getItemInHand().getType().equals(org.bukkit.Material.FLINT_AND_STEEL)) {
            x = interactEvent.getClickedBlock().getLocation().getX();
            x = interactEvent.getClickedBlock().getLocation().getY();
            z = interactEvent.getClickedBlock().getLocation().getZ();
            action = eu.mcone.gameapi.api.replay.packets.player.block.Action.fromNMS(interactEvent.getAction());
            return new Object[]{interactEvent.getPlayer()};
        } else if (interactEvent.getAction().equals(Action.LEFT_CLICK_AIR)
                || interactEvent.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            action = eu.mcone.gameapi.api.replay.packets.player.block.Action.fromNMS(interactEvent.getAction());
            return new Object[]{interactEvent.getPlayer()};
        }

        return null;
    }

    @Override
    public void encode(PlayerRunner runner) {
        if (action != null) {
            switch (action) {
                case LEFT_CLICK_AIR:
                case LEFT_CLICK_BLOCK:
                    runner.getPlayer().getNpc().sendAnimation(NpcAnimation.SWING_ARM, runner.getWatchers().toArray(new Player[0]));
                    break;
                case RIGHT_CLICK_BLOCK:
                    CoreLocation location = new CoreLocation(runner.getPlayer().getNpc().getLocation());
                    location.setX(x);
                    location.setZ(y);
                    location.setZ(z);

                    EntityTNTPrimed tnt = new EntityTNTPrimed(((CraftWorld) location.bukkit().getWorld()).getHandle());
                    tnt.setPosition(location.getX(), location.getY(), location.getZ());
                    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(tnt, 50);

                    for (Player player : runner.getWatchers()) {
                        player.sendBlockChange(location.bukkit(), Material.AIR, (byte) 0);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnEntity);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeByte(action.getId());
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException {
        action = eu.mcone.gameapi.api.replay.packets.player.block.Action.getWhereID(in.readByte());
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
