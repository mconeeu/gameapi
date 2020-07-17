package eu.mcone.gameapi.api.replay.packets.player.objective;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreObjectiveCreateEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CoreObjectiveCreateEventCodec extends Codec<CoreObjectiveCreateEvent, ReplayPlayer> {

    private String name;

    public CoreObjectiveCreateEventCodec() {
        super("Scoreboard", CoreObjectiveCreateEvent.class, ReplayPlayer.class);
    }

    @Override
    public Object[] decode(Player player, CoreObjectiveCreateEvent objectiveCreateEvent) {
        name = objectiveCreateEvent.getSidebarObjective().getName();
        return new Object[]{player};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.setScoreboard(new ReplayPlayerSidebarObjective(name));
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(name);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        name = in.readUTF();
    }
}
