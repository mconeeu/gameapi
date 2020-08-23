package eu.mcone.gameapi.api.replay;

import com.mongodb.client.FindIterable;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.Option;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface ReplayManager {

    CodecRegistry getCodecRegistry();

    eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader();

    eu.mcone.gameapi.api.replay.ReplayViewManager getReplayViewManager();

    ReplayRecord createReplay(String gameID);

    ReplayRecord createReplay(String ID, String gameID);

    void saveReplay(final ReplayRecord replayRecord);

    boolean deleteReplay(final String sessionID);

    Replay getReplay(final String replayID);

    FindIterable<Replay> getReplaysForPlayer(final UUID uuid, final int skip, final int limit);

    long countReplaysForGamemodeAndPlayer(Player player, Gamemode gamemode);

    long countReplaysForGamemode(Gamemode gamemode);

    boolean existsReplay(final String replayID);

    long getReplaySize();

    long countReplaysForPlayer(final Player player);

    FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(Gamemode gamemode);

    FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(Gamemode gamemode, final int skip, final int limit);

    FindIterable<Replay> getReplays(Player player, Gamemode gamemode, int skip, int limit);

    FindIterable<Replay> getReplays(final int row, final int limit);

    List<ReplayRecord> getRecording();
}
