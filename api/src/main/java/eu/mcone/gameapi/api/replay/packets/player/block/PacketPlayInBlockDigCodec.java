package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketPlayInBlockDigCodec extends Codec<PacketPlayInBlockDig, PlayerRunner> {

    private PacketPlayInBlockDig.EnumPlayerDigType action;

    public PacketPlayInBlockDigCodec() {
        super("BlockDig");
    }

    @Override
    public Object[] decode(Player player, PacketPlayInBlockDig blockDig) {
        action = blockDig.c();
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        switch (action) {
            case START_DESTROY_BLOCK:
                runner.setBreaking(true);
                break;
            case STOP_DESTROY_BLOCK:
                runner.setBreaking(false);
                break;
        }
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(action.toString());
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        PacketPlayInBlockDig.EnumPlayerDigType.valueOf(in.readUTF());
    }
}
