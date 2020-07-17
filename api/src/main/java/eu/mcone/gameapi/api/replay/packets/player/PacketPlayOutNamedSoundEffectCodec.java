package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.coresystem.api.bukkit.util.SoundUtils;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class PacketPlayOutNamedSoundEffectCodec extends Codec<PacketPlayOutNamedSoundEffect, PlayerRunner> {

    private String id;
    private double x;
    private double y;
    private double z;

    public PacketPlayOutNamedSoundEffectCodec() {
        super("Sound", PacketPlayOutNamedSoundEffect.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, PacketPlayOutNamedSoundEffect namedSoundEffect) {
        id = ReflectionManager.getValue(namedSoundEffect, "a", String.class);
        x = ReflectionManager.getValue(namedSoundEffect, "b", int.class);
        y = ReflectionManager.getValue(namedSoundEffect, "c", int.class);
        z = ReflectionManager.getValue(namedSoundEffect, "d", int.class);

        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        CoreLocation location = new CoreLocation(runner.getPlayer().getNpc().getLocation());
        location.setX(x);
        location.setY(y);
        location.setZ(z);

        SoundUtils.playSound(id, location.bukkit(), runner.getWatchers().toArray(new Player[0]));
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(id);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        id = in.readUTF();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
