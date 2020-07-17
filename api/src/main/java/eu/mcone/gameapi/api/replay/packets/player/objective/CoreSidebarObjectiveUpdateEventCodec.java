package eu.mcone.gameapi.api.replay.packets.player.objective;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreSidebarObjectiveUpdateEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CoreSidebarObjectiveUpdateEventCodec extends Codec<CoreSidebarObjectiveUpdateEvent, ReplayPlayer> {

    private Map<Integer, String> scores;

    public CoreSidebarObjectiveUpdateEventCodec() {
        super("UpdateObjective", CoreSidebarObjectiveUpdateEvent.class, ReplayPlayer.class);
    }

    @Override
    public Object[] decode(Player player, CoreSidebarObjectiveUpdateEvent objectiveUpdateEvent) {
        scores = objectiveUpdateEvent.getCoreSidebarObjectiveEntry().getScores();
        return new Object[]{player};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.getScoreboard().setCachedScores(scores);
        replayPlayer.getScoreboard().reload();
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUnshared(scores);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        scores = ((HashMap<Integer, String>) in.readUnshared());
    }
}
