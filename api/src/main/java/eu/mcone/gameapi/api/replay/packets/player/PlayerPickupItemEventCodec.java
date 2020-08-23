package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.AsyncPlayerRunner;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.*;

@Getter
public class PlayerPickupItemEventCodec extends Codec<PlayerPickupItemEvent, PlayerRunner> {

    public static final byte CODEC_VERSION = 1;

    private int entityID;

    public PlayerPickupItemEventCodec() {
        super((byte) 11, (byte) 3);
    }

    @Override
    public Object[] decode(Player player, PlayerPickupItemEvent pickupItemEvent) {
        entityID = pickupItemEvent.getItem().getEntityId();
        return new Object[]{pickupItemEvent.getPlayer()};
    }

    @Override
    public void encode(PlayerRunner runner) {
        if (entityID != 0) {
            System.out.println("ENTITY ID != 0");
            int id = runner.getContainer().getEntities().getOrDefault(entityID, 0);
            if (id != 0) {
                System.out.println("ID != 0");
                for (Player player : runner.getViewers()) {
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(id));
                }
            }
        }
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeInt(entityID);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException, ClassNotFoundException {
        entityID = in.readInt();
    }
}
