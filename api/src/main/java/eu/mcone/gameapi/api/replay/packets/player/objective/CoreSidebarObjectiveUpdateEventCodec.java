package eu.mcone.gameapi.api.replay.packets.player.objective;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreSidebarObjectiveUpdateEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CoreSidebarObjectiveUpdateEventCodec extends Codec<CoreSidebarObjectiveUpdateEvent, ReplayPlayer> {

    private Map<Integer, String> scores;

    public CoreSidebarObjectiveUpdateEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, CoreSidebarObjectiveUpdateEvent objectiveUpdateEvent) {
        scores = objectiveUpdateEvent.getCoreSidebarObjectiveEntry().getScores();
        return new Object[]{objectiveUpdateEvent.getPlayer()};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.getScoreboard().setCachedScores(scores);
        replayPlayer.getScoreboard().reload();
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeUTF(CoreSystem.getInstance().getGson().toJson(scores));
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException {
        scores = CoreSystem.getInstance().getGson().fromJson(in.readUTF(), HashMap.class);
    }
}
