package eu.mcone.gameapi.api.replay.session;

import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.Option;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface ReplayManager {

    CodecRegistry getCodecRegistry();

    void registerCommand();

    eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader();

    ReplayRecord createReplay(final String ID, final Gamemode gamemode, Option... options);

    ReplayRecord createReplay(final Gamemode gamemode, Option... options);

    void saveReplay(final ReplayRecord replayRecord);

    boolean deleteSession(final String sessionID);

    eu.mcone.gameapi.api.replay.session.Replay getReplay(final String replayID);

    List<Replay> getReplaysForPlayer(final UUID uuid, final int row, final int limit);

    boolean existsReplay(final String replayID);

    long getReplaySize();

    long getPlayerReplaySize(final Player player);

    List<Replay> getReplays(final int row, final int limit);

    List<ReplayRecord> getRecording();
}
