package eu.mcone.gameapi.replay.session;

import com.mongodb.client.MongoCollection;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.LocationCodecProvider;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionAlreadyExistsException;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionNotFoundException;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.replay.npc.ReplayNpcManager;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ReplaySessionManager implements eu.mcone.gameapi.api.replay.session.ReplaySessionManager {

    @Getter
    private final MongoCollection<eu.mcone.gameapi.replay.session.ReplaySession> replayCollection = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), new LocationCodecProvider(), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("replay_sessions", eu.mcone.gameapi.replay.session.ReplaySession.class);

    private HashMap<String, ReplaySession> replaySessions;

    @Getter
    private String sessionID;
    @Getter
    private ReplayNpcManager npcManager;

    public ReplaySessionManager(final GamePlugin plugin, final Option... options) {
        replaySessions = new HashMap<>();
        sessionID = generateSessionID();
        npcManager = new ReplayNpcManager();

        GameAPIPlugin.getSystem().sendConsoleMessage("§aLoading Replay SessionManager...");
        if (Arrays.asList(options).contains(Option.SESSION_MANAGER_LOAD_ALL_REPLAYS)) {
            load();
        }
        System.out.println(replaySessions);
    }

    /**
     * Loads all ReplaySessions form the DB
     */
    private void load() {
        for (Document entry : CoreSystem.getInstance().getMongoDB().getCollection("replay_sessions", Document.class).find().projection(include("info.world"))) {
            String world = entry.get("info", Document.class).getString("world");

            if (CoreSystem.getInstance().getWorldManager().getWorld(world) == null) {
                CoreSystem.getInstance().getWorldManager().download(world);
            }
        }

        for (ReplaySession session : replayCollection.find()) {
            replaySessions.put(session.getID(), session);
        }
    }

    public void saveSession(final ReplaySession session) {
        try {
            String sessionID = session.getID();
            if (!existsSession(sessionID)) {
                //Save date
                session.getInfo().setStopped(System.currentTimeMillis() / 1000);

                if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(session.getInfo().getWorld())) {
                    boolean succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(session.getInfo().getWorld()));

                    if (succeed) {
                        replaySessions.put(sessionID, session);
                        replayCollection.insertOne((eu.mcone.gameapi.replay.session.ReplaySession) session);
                    } else {
                        System.out.println("ERROR");
                    }
                } else {
                    replaySessions.put(sessionID, session);
                    replayCollection.insertOne((eu.mcone.gameapi.replay.session.ReplaySession) session);
                }
            } else {
                throw new ReplaySessionAlreadyExistsException("The replay with the sessionID " + sessionID + " already exists!");
            }
        } catch (ReplaySessionAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteSession(final String sessionID) {
        if (replaySessions.containsKey(sessionID)) {
            replaySessions.remove(sessionID);
            replayCollection.deleteOne(eq("sessionID", sessionID));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a ReplaySession object for the sessionID
     *
     * @param sessionID Unique ID
     * @return ReplaySession
     */
    public ReplaySession getSession(final String sessionID) {
        try {
            if (replaySessions.containsKey(sessionID)) {
                return replaySessions.get(sessionID);
            } else {
                ReplaySession session = replayCollection.find(eq("sessionID", sessionID)).first();

                if (session != null) {
                    replaySessions.put(sessionID, session);
                    return session;
                } else {
                    throw new ReplaySessionNotFoundException();
                }
            }
        } catch (ReplaySessionNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a List of ReplaySessions for the specified UUID (Player)
     *
     * @param uuid Unique UUID
     * @return List of ReplaySessions
     */
    public List<ReplaySession> getSessionsForPlayer(final UUID uuid) {
        List<ReplaySession> sessions = new ArrayList<>();
        for (ReplaySession session : replaySessions.values()) {
            ReplayPlayer replayPlayer = session.getReplayPlayer(uuid);

            if (replayPlayer != null) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    /**
     * Checks if the specified sessionID already exists
     *
     * @param sessionID Unique ID
     * @return boolean
     */
    public boolean existsSession(final String sessionID) {
        if (replaySessions.containsKey(sessionID)) {
            return true;
        } else {
            ReplaySession replaySession = replayCollection.find(eq("sessionID", sessionID)).first();
            return replaySession != null;
        }
    }

    /**
     * Returns a list of all replay sessions in the database
     *
     * @return ReplaySession
     */
    public Collection<ReplaySession> getReplaySessions() {
        return replaySessions.values();
    }

    /**
     * Generates a unique SessionID for the current replay
     *
     * @return Unique SessionID as String
     */
    private String generateSessionID() {
        StringBuilder uuid = new StringBuilder();
        String[] uuidArray = UUID.randomUUID().toString().split("-");

        Random random = new Random(0);

        for (int i = 0; i < uuidArray.length / 2; i++) {
            uuid.append(uuidArray[random.nextInt(uuidArray.length)]);
        }

        return uuid.toString();
    }
}
