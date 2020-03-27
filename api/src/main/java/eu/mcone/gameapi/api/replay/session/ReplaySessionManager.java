package eu.mcone.gameapi.api.replay.session;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReplaySessionManager {

    boolean isCache();

    String getSessionID();

    eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader();

    void saveSession(final ReplaySession replaySession);

    boolean deleteSession(final String sessionID);

    ReplaySession getLiveSession(final String sessionID);

    ReplaySession getSession(final String replaySessionID);

    List<ReplaySession> getSessionsForPlayer(final UUID uuid);

    boolean existsSession(final String sessionID);

    Collection<ReplaySession> getLiveSessions();

    Collection<ReplaySession> getLiveSessions(int startIndex, int values);

    Collection<ReplaySession> getSessions();
}
