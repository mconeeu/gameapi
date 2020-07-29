package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.*;

public class PlayerDeathEventCodec extends Codec<PlayerDeathEvent, PlayerRunner> {

    public PlayerDeathEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PlayerDeathEvent playerDeathEvent) {
        return new Object[]{playerDeathEvent.getEntity()};
    }

    @Override
    public void encode(PlayerRunner runner) {
        runner.getPlayer().getNpc().togglePlayerVisibility(ListMode.BLACKLIST, runner.getWatchers().toArray(new Player[0]));
    }

    @Override
    protected void onWriteObject(DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    protected void onReadObject(DataInputStream dataInputStream) throws IOException, ClassNotFoundException {

    }
}
