package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplayEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class PlayerQuitReplayEventCodec extends Codec<PlayerQuitReplayEvent, PlayerNpc> {

    public PlayerQuitReplayEventCodec() {
        super("DestroyNpc", PlayerQuitReplayEvent.class, PlayerNpc.class);
    }

    @Override
    public Object[] decode(Player player, PlayerQuitReplayEvent replayEvent) {
        return new Object[]{player};
    }

    @Override
    public void encode(PlayerNpc playerNpc) {
        CoreSystem.getInstance().getNpcManager().removeNPC(playerNpc);
        Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(playerNpc, NpcAnimationStateChangeEvent.NpcAnimationState.END));
    }

    @Override
    protected void onWriteObject(ObjectOutputStream objectOutputStream) {
    }

    @Override
    protected void onReadObject(ObjectInputStream objectInputStream) {
    }
}
