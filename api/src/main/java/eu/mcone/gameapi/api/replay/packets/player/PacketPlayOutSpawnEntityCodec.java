package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.craftbukkit.v1_8_R3.metadata.EntityMetadataStore;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

public class PacketPlayOutSpawnEntityCodec extends Codec<PacketPlayOutSpawnEntity, PlayerRunner> {

    private int id;
    private int typ; //j
    private int data; //k

    private int x;
    private int y;
    private int z;
    private int yaw;
    private int pitch;

    public PacketPlayOutSpawnEntityCodec() {
        super("SpawnEntity");
    }

    @Override
    public Object[] decode(Player player, PacketPlayOutSpawnEntity spawnEntity) {
        id = ReflectionManager.getValue(spawnEntity, "a", int.class);
        typ = ReflectionManager.getValue(spawnEntity, "j", int.class);

//        EntityType.fromId()
        data = ReflectionManager.getValue(spawnEntity, "k", int.class);

        x = ReflectionManager.getValue(spawnEntity, "b", int.class);
        y = ReflectionManager.getValue(spawnEntity, "c", int.class);
        z = ReflectionManager.getValue(spawnEntity, "d", int.class);
        yaw = ReflectionManager.getValue(spawnEntity, "h", int.class);
        pitch = ReflectionManager.getValue(spawnEntity, "i", int.class);

        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity();
        int id = getNextEntityId();
        ReflectionManager.setValue(spawnEntity, "a", id);
        ReflectionManager.setValue(spawnEntity, "j", typ);
        ReflectionManager.setValue(spawnEntity, "k", data);
        ReflectionManager.setValue(spawnEntity, "b", x);
        ReflectionManager.setValue(spawnEntity, "c", y);
        ReflectionManager.setValue(spawnEntity, "d", z);
        ReflectionManager.setValue(spawnEntity, "h", yaw);
        ReflectionManager.setValue(spawnEntity, "i", pitch);

        runner.getContainer().getEntities().put(this.id, id);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeInt(typ);
        out.writeInt(data);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeInt(yaw);
        out.writeInt(pitch);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        id = in.readInt();
        typ = in.readInt();
        data = in.readInt();
        x = in.readInt();
        y = in.readInt();
        z = in.readInt();
        yaw = in.readInt();
        pitch = in.readInt();
    }

    private static synchronized int getNextEntityId() {
        try {
            Field field = Entity.class.getDeclaredField("entityCount");
            field.setAccessible(true);
            int id = field.getInt(null);
            field.set(null, id + 1);
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
