package eu.mcone.gameapi.api.replay.session;

import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.Option;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReplayManager {

    boolean isCache();

    eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader();

    ReplayRecord createReplay(final String ID, final Gamemode gamemode, Option... options);

    ReplayRecord createReplay(final Gamemode gamemode, Option... options);

    void saveReplay(final ReplayRecord replayRecord);

    boolean deleteSession(final String sessionID);

    eu.mcone.gameapi.api.replay.session.Replay getReplay(final String replayID);

    List<Replay> getReplaysForPlayer(final UUID uuid);

    boolean existsReplay(final String replayID);

    Collection<Replay> getCachedReplays();

    long getReplaySize();

    Collection<Replay> getReplays();

    Collection<eu.mcone.gameapi.api.replay.session.Replay> getReplay(int startIndex, int values);

    Collection<eu.mcone.gameapi.api.replay.session.Replay> getReplay();
}
