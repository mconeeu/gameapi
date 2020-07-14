package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateHealth;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class PlayOutUpdateHealthCodec extends Codec<PacketPlayOutUpdateHealth, ReplayPlayer> {

    private int health;
    private int food;

    public PlayOutUpdateHealthCodec() {
        super("HEALTH");
    }

    @Override
    public Object[] decode(Player player, PacketPlayOutUpdateHealth updateHealth) {
        this.health = ReflectionManager.getValue(updateHealth, "a", Integer.class);
        this.food = ReflectionManager.getValue(updateHealth, "b", Integer.class);

        return new Object[]{player};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.setHealth(health);
        replayPlayer.setFood(food);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(health);
        out.writeInt(food);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        health = in.readInt();
        food = in.readInt();
    }
}
