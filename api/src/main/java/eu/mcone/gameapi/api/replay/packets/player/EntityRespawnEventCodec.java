package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EntityRespawnEventCodec extends Codec<PlayerRespawnEvent, PlayerRunner> {

    private double x;
    private double y;
    private double z;

    public EntityRespawnEventCodec() {
        super("Respawn");
    }

    @Override
    public Object[] decode(Player player, PlayerRespawnEvent playerRespawnEvent) {

        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().getNpc().setItemInHand(null);
        runner.getPlayer().getNpc().togglePlayerVisibility(ListMode.WHITELIST, runner.getWatchers().toArray(new Player[0]));
        CoreLocation location = new CoreLocation(runner.getPlayer().getNpc().getLocation());
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        runner.getPlayer().getNpc().teleport(location);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
