package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.*;

@Getter
public class StatsChangeEventCodec extends Codec<PlayerRoundStatsChangeEvent, ReplayPlayer> {

    private int kills;
    private int deaths;
    private int goals;

    public StatsChangeEventCodec() {
        super((byte) 0, (byte) 0);
    }

    @Override
    public Object[] decode(Player player, PlayerRoundStatsChangeEvent statsChangeEvent) {
        kills = statsChangeEvent.getKills();
        deaths = statsChangeEvent.getDeaths();
        goals = statsChangeEvent.getGoals();

        return new Object[]{statsChangeEvent.getPlayer()};
    }

    @Override
    public void encode(ReplayPlayer replayPlayer) {
        replayPlayer.getStats().setKills(kills);
        replayPlayer.getStats().setDeaths(deaths);
        replayPlayer.getStats().setGoals(goals);
    }

    @Override
    protected void onWriteObject(DataOutputStream out) throws IOException {
        out.writeInt(kills);
        out.write(deaths);
        out.writeInt(goals);
    }

    @Override
    protected void onReadObject(DataInputStream in) throws IOException, ClassNotFoundException {
        kills = in.readInt();
        deaths = in.readInt();
        goals = in.readInt();
    }
}
