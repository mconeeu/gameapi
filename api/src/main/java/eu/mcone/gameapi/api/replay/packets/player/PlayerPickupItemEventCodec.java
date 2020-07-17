package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class PlayerPickupItemEventCodec extends Codec<PlayerPickupItemEvent, PlayerRunner> {

    private int entityID;

    public PlayerPickupItemEventCodec() {
        super("Pickup", PlayerPickupItemEvent.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, PlayerPickupItemEvent pickupItemEvent) {
        entityID = pickupItemEvent.getItem().getEntityId();
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (Player player : runner.getWatchers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityID));
        }
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(entityID);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        entityID = in.readInt();
    }
}
