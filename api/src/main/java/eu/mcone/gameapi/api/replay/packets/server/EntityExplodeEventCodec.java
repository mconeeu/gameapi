package eu.mcone.gameapi.api.replay.packets.server;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import eu.mcone.gameapi.api.replay.runner.ServerRunner;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EntityExplodeEventCodec extends Codec<EntityExplodeEvent, ServerRunner> {

    private List<CoreLocation> destroy;

    public EntityExplodeEventCodec() {
        super("Explode", EntityExplodeEvent.class, ServerRunner.class);
        destroy = new ArrayList<>();
    }

    @Override
    public Object[] decode(Player player, EntityExplodeEvent entityExplodeEvent) {
        if (entityExplodeEvent.getEntityType().equals(EntityType.PRIMED_TNT)) {
            for (Block block : entityExplodeEvent.blockList()) {
                destroy.add(new CoreLocation(block.getLocation()));
            }

            return new Object[]{player};
        }

        return null;
    }

    @Override
    public void encode(ServerRunner runner) {
        for (CoreLocation location : destroy) {
            for (Player player : runner.getWatchers()) {
                player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
                player.sendBlockChange(location.bukkit(), Material.AIR, (byte) 0);
            }
        }
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUnshared(destroy);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        destroy = (List<CoreLocation>) in.readUnshared();
    }
}
