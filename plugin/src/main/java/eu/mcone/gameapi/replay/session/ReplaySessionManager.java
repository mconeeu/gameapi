package eu.mcone.gameapi.replay.session;

import com.mongodb.client.MongoCollection;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.LocationCodecProvider;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionAlreadyExistsException;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionNotFoundException;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.api.utils.IDUtils;
import eu.mcone.gameapi.replay.world.WorldDownloader;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ReplaySessionManager implements eu.mcone.gameapi.api.replay.session.ReplaySessionManager {

    @Getter
    private final MongoCollection<eu.mcone.gameapi.replay.session.ReplaySession> replayCollection = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), new LocationCodecProvider(), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("replay_sessions", eu.mcone.gameapi.replay.session.ReplaySession.class);

    @Getter
    private boolean cache = true;

    private HashMap<String, ReplaySession> replaySessions;
    private WorldDownloader worldDownloader;

    @Getter
    private String sessionID;

    public ReplaySessionManager(final Option... options) {
        replaySessions = new HashMap<>();
        sessionID = IDUtils.generateID();

        List<Option> optionsList = Arrays.asList(options);

        GameAPIPlugin.getSystem().sendConsoleMessage("§aLoading Replay SessionManager...");
        if (optionsList.contains(Option.REPLAY_SESSION_MANAGER_LOAD_ALL_REPLAYS)) {
            load();
        } else {
            cache = false;
        }

        if (optionsList.contains(Option.REPLAY_SESSION_MANAGER_USE_WORLD_DOWNLOADER)) {
            GameAPIPlugin.getSystem().sendConsoleMessage("§aStarting world downloader...");
            worldDownloader = new WorldDownloader();
            worldDownloader.runDownloader();
        }
    }

    public eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader() {
        return worldDownloader;
    }

    /**
     * Loads all ReplaySessions form the DB
     */
    private void load() {
        for (ReplaySession session : replayCollection.find()) {
            replaySessions.put(session.getID(), session);
        }
    }

    @Override
    public void saveSession(final ReplaySession session) {
        try {
            System.out.println("SAVE REPLAY");
            String sessionID = session.getID();
            if (!existsSession(sessionID)) {
                //Save date
                session.getInfo().setStopped(System.currentTimeMillis() / 1000);

                for (ReplayPlayer player : session.getPlayers()) {
                    player.getData().setSessionID(sessionID);
                }

                boolean succeed = true;
                if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(session.getInfo().getWorld())) {
                    succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(session.getInfo().getWorld()));
                }

                if (succeed) {
                    System.out.println("Succeed, create and save now the replay file...");
                    replaySessions.put(sessionID, session);
                    replayCollection.insertOne((eu.mcone.gameapi.replay.session.ReplaySession) session);

                    Bukkit.getScheduler().runTaskAsynchronously(GameAPIPlugin.getInstance(), () -> {
                        List<File> zipFiles = new ArrayList<>();

                        for (Map.Entry<Integer, ReplayChunk> entry : session.getReplayRecorder().getChunks().entrySet()) {
                            File file = new File("CHUNK:" + entry.getKey());
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(entry.getValue().compressData());
                                zipFiles.add(file);
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/" + sessionID + ".replay");
                            ZipOutputStream zipOut = new ZipOutputStream(fos);
                            for (File file : zipFiles) {
                                FileInputStream fis = new FileInputStream(file);
                                ZipEntry zipEntry = new ZipEntry(file.getName());
                                zipOut.putNextEntry(zipEntry);

                                byte[] bytes = new byte[1024];
                                int length;
                                while ((length = fis.read(bytes)) >= 0) {
                                    zipOut.write(bytes, 0, length);
                                }

                                fis.close();
                                file.delete();
                            }

                            zipOut.close();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                throw new ReplaySessionAlreadyExistsException("The replay with the sessionID " + sessionID + " already exists!");
            }
        } catch (ReplaySessionAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteSession(final String sessionID) {
        if (replaySessions.containsKey(sessionID)) {
            replaySessions.remove(sessionID);
            replayCollection.deleteOne(eq("iD", sessionID));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the live version of the replay session from the database
     *
     * @param sessionID Unique ID
     * @return ReplaySession interface
     */
    @Override
    public ReplaySession getLiveSession(final String sessionID) {
        try {
            ReplaySession session = replayCollection.find(eq("iD", sessionID)).first();

            if (session != null) {
                download(session.getInfo().getWorld());
                return session;
            } else {
                throw new ReplaySessionNotFoundException();
            }
        } catch (ReplaySessionNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a ReplaySession object for the sessionID
     *
     * @param sessionID Unique ID
     * @return ReplaySession
     */
    @Override
    public ReplaySession getSession(final String sessionID) {
        if (replaySessions.containsKey(sessionID)) {
            return replaySessions.get(sessionID);
        } else {
            return getLiveSession(sessionID);
        }
    }

    /**
     * Returns a List of ReplaySessions for the specified UUID (Player)
     *
     * @param uuid Unique UUID
     * @return List of ReplaySessions
     */
    @Override
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
    @Override
    public boolean existsSession(final String sessionID) {
        if (replaySessions.containsKey(sessionID)) {
            return true;
        } else {
            ReplaySession replaySession = replayCollection.find(eq("ID", sessionID)).first();
            return replaySession != null;
        }
    }

    @Override
    public Collection<ReplaySession> getLiveSessions() {
        List<ReplaySession> sessions = new ArrayList<>();
        replayCollection.find().iterator().forEachRemaining(sessions::add);
        return sessions;
    }

    @Override
    public Collection<ReplaySession> getLiveSessions(int startIndex, int values) {
        List<ReplaySession> sessions = new ArrayList<>();
        replayCollection.find().skip(startIndex).limit(values).iterator().forEachRemaining(sessions::add);
        return sessions;
    }

    /**
     * Returns a list of all replay sessions in the database
     *
     * @return ReplaySession
     */
    @Override
    public Collection<ReplaySession> getSessions() {
        return replaySessions.values();
    }

    private void download(String world) {
        if (!CoreSystem.getInstance().getWorldManager().existWorld(world)) {
            if (worldDownloader != null) {
                worldDownloader.getDownloaded().add(world);
            }

            CoreSystem.getInstance().getWorldManager().download(world);
            CoreWorld downloadedWorld = CoreSystem.getInstance().getWorldManager().getWorld(world);
            downloadedWorld.setLoadOnStartup(false);
            downloadedWorld.save();
        }
    }
}
