package eu.mcone.gameapi.api.replay.session;

import eu.mcone.gameapi.api.replay.npc.ReplayNpcManager;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReplaySessionManager {

    String getSessionID();

    ReplayNpcManager getNpcManager();

    void saveSession(final ReplaySession replaySession);

    boolean deleteSession(final String sessionID);

    ReplaySession getSession(final String replaySessionID);

    List<ReplaySession> getSessionsForPlayer(final UUID uuid);

    boolean existsSession(final String sessionID);

    Collection<ReplaySession> getReplaySessions();
}
