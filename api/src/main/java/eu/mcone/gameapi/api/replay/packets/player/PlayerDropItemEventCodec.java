package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.io.*;

@Getter
public class PlayerDropItemEventCodec extends Codec<PlayerDropItemEvent, PlayerRunner> {

    private int entityID;
    private double x;
    private double y;
    private double z;

    public PlayerDropItemEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PlayerDropItemEvent dropItemEvent) {
        entityID = dropItemEvent.getItemDrop().getEntityId();
        x = dropItemEvent.getItemDrop().getLocation().getX();
        y = dropItemEvent.getItemDrop().getLocation().getY();
        z = dropItemEvent.getItemDrop().getLocation().getZ();
        return new Object[]{dropItemEvent.getPlayer()};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (Player player : runner.getWatchers()) {
            PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity();
            ReflectionManager.setValue(spawnEntity, "a", entityID);
            ReflectionManager.setValue(spawnEntity, "b", x);
            ReflectionManager.setValue(spawnEntity, "c", y);
            ReflectionManager.setValue(spawnEntity, "d", z);
            ReflectionManager.setValue(spawnEntity, "h", 0);
            ReflectionManager.setValue(spawnEntity, "i", 0);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnEntity);
        }
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeInt(entityID);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException, ClassNotFoundException {
        entityID = in.readInt();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
