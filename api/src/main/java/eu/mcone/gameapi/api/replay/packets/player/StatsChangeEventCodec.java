package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StatsChangeEventCodec extends Codec<PlayerRoundStatsChangeEvent, ReplayPlayer> {

    private int kills;
    private int deaths;
    private int goals;

    public StatsChangeEventCodec() {
        super("STATS");
    }

    @Override
    public Object[] decode(Player player, PlayerRoundStatsChangeEvent statsChangeEvent) {
        kills = statsChangeEvent.getKills();
        deaths = statsChangeEvent.getDeaths();
        goals = statsChangeEvent.getGoals();

        return new Object[]{player};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.getStats().setKills(kills);
        replayPlayer.getStats().setDeaths(deaths);
        replayPlayer.getStats().setGoals(goals);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(kills);
        out.write(deaths);
        out.writeInt(goals);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        kills = in.readInt();
        deaths = in.readInt();
        goals = in.readInt();
    }
}
