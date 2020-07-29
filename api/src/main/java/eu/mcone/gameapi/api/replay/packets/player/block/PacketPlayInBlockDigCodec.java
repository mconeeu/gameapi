package eu.mcone.gameapi.api.replay.packets.player.block;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

import java.io.*;

public class PacketPlayInBlockDigCodec extends Codec<PacketPlayInBlockDig, PlayerRunner> {

    private EnumPlayerDigType action;

    public PacketPlayInBlockDigCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PacketPlayInBlockDig blockDig) {
        switch (blockDig.c()) {
            case START_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                action = EnumPlayerDigType.fromNMS(blockDig.c());
                break;
        }

        return (action != null ? new Object[]{player} : null);
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
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeByte(action.getId());
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException {
        action = EnumPlayerDigType.getWhereID(in.readByte());
    }
}
