package eu.mcone.gameapi.replay.session;

import com.mongodb.client.MongoCollection;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.LocationCodecProvider;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionAlreadyExistsException;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionNotFoundException;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
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

public class ReplayManager implements eu.mcone.gameapi.api.replay.session.ReplayManager {

    @Getter
    private final MongoCollection<Replay> replayCollection = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), new LocationCodecProvider(), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("replay", Replay.class);

    @Getter
    private final boolean cache;
    private final HashMap<String, eu.mcone.gameapi.api.replay.session.Replay> replay;
    private WorldDownloader worldDownloader;

    public ReplayManager(final Option... options) {
        replay = new HashMap<>();

        List<Option> optionsList = Arrays.asList(options);

        GameAPIPlugin.getSystem().sendConsoleMessage("§aLoading Replay Manager...");
        if (optionsList.contains(Option.REPLAY_SESSION_MANAGER_LOAD_ALL_REPLAYS)) {
            cache = true;
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
        for (Replay replay : replayCollection.find()) {
            this.replay.put(replay.getID(), replay);
        }
    }

    public ReplayRecord createReplay(final String ID, final Gamemode gamemode, Option... options) {
        return new eu.mcone.gameapi.replay.session.ReplayRecord(ID, gamemode, options);
    }

    public ReplayRecord createReplay(final Gamemode gamemode, Option... options) {
        return new eu.mcone.gameapi.replay.session.ReplayRecord(gamemode, options);
    }

    @Override
    public void saveReplay(final ReplayRecord replayRecord) {
        try {
            String replayID = replayRecord.getID();
            if (!existsReplay(replayID)) {
                Replay replay = new Replay(replayRecord);
                replay.getChunkHandler().save();
                this.replay.put(replay.getID(), replay);
                replayCollection.insertOne(replay);
            } else {
                throw new ReplaySessionAlreadyExistsException("The replay with the sessionID " + replayID + " already exists!");
            }
        } catch (ReplaySessionAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteSession(final String sessionID) {
        if (replay.containsKey(sessionID)) {
            replay.remove(sessionID);
            replayCollection.deleteOne(eq("ID", sessionID));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the live version of the replay session from the database
     *
     * @param replayID Unique ID
     * @return ReplaySession interface
     */
    @Override
    public eu.mcone.gameapi.api.replay.session.Replay getReplay(final String replayID) {
        try {
            if (replay.containsKey(replayID)) {
                return replay.get(replayID);
            } else {
                Replay replay = replayCollection.find(eq("ID", replayID)).first();

                if (replay != null) {
                    download(replay.getWorld());
                    return replay;
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
     * Returns a List of Replays for the specified UUID (Player) only from the local cache.
     *
     * @param uuid Unique UUID
     * @return List of ReplaySessions
     */
    @Override
    public List<eu.mcone.gameapi.api.replay.session.Replay> getReplaysForPlayer(final UUID uuid) {
        List<eu.mcone.gameapi.api.replay.session.Replay> replays = new ArrayList<>();
        for (eu.mcone.gameapi.api.replay.session.Replay replay : replay.values()) {
            ReplayPlayer replayPlayer = replay.getReplayPlayer(uuid);

            if (replayPlayer != null) {
                replays.add(replay);
            }
        }

        return replays;
    }

    /**
     * Checks if the specified sessionID already exists
     *
     * @param replayID Unique ID
     * @return boolean
     */
    @Override
    public boolean existsReplay(final String replayID) {
        if (replay.containsKey(replayID)) {
            return true;
        } else {
            return replayCollection.find(eq("ID", replayID)).first() != null;
        }
    }

    @Override
    public Collection<eu.mcone.gameapi.api.replay.session.Replay> getCachedReplays() {
        return replay.values();
    }

    public long getReplaySize() {
        return replayCollection.countDocuments();
    }

    @Override
    public Collection<eu.mcone.gameapi.api.replay.session.Replay> getReplays() {
        List<eu.mcone.gameapi.api.replay.session.Replay> sessions = new ArrayList<>();
        replayCollection.find().iterator().forEachRemaining(sessions::add);
        return sessions;
    }

    @Override
    public Collection<eu.mcone.gameapi.api.replay.session.Replay> getReplay(int startIndex, int values) {
        List<eu.mcone.gameapi.api.replay.session.Replay> replays = new ArrayList<>();
        replayCollection.find().skip(startIndex).limit(values).iterator().forEachRemaining(replays::add);
        return replays;
    }

    /**
     * Returns a list of all replay sessions in the database
     *
     * @return Replay
     */
    @Override
    public Collection<eu.mcone.gameapi.api.replay.session.Replay> getReplay() {
        return replay.values();
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
