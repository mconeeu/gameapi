package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayerDeathEventCodec extends Codec<PlayerDeathEvent, PlayerRunner> {

    public PlayerDeathEventCodec() {
        super("Death");
    }

    @Override
    public Object[] decode(Player player, PlayerDeathEvent playerDeathEvent) {
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().getNpc().togglePlayerVisibility(ListMode.BLACKLIST, runner.getWatchers().toArray(new Player[0]));
    }

    @Override
    protected void onWriteObject(ObjectOutputStream objectOutputStream) {
    }

    @Override
    protected void onReadObject(ObjectInputStream objectInputStream) {
    }
}
