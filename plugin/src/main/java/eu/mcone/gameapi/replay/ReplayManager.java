package eu.mcone.gameapi.replay;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.LocationCodecProvider;
import eu.mcone.coresystem.api.bukkit.event.StatsChangeEvent;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreObjectiveCreateEvent;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreSidebarObjectiveUpdateEvent;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.npc.capture.codecs.*;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.overwatch.report.Report;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.ReplayRecord;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplayEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplayEvent;
import eu.mcone.gameapi.api.replay.exception.GameModuleNotActiveException;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionNotFoundException;
import eu.mcone.gameapi.api.replay.packets.player.*;
import eu.mcone.gameapi.api.replay.packets.player.block.BlockBreakEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.BlockPlaceEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.PacketPlayInBlockDigCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.PlayerInteractEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.inventory.InventoryCloseEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.objective.CoreObjectiveCreateEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.objective.CoreSidebarObjectiveUpdateEventCodec;
import eu.mcone.gameapi.api.replay.packets.server.EntityExplodeEventCodec;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import eu.mcone.gameapi.command.ReplayCMD;
import eu.mcone.gameapi.replay.chunk.ChunkHandler;
import eu.mcone.gameapi.replay.runner.server.SyncServerRunner;
import eu.mcone.gameapi.replay.world.WorldDownloader;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ReplayManager implements eu.mcone.gameapi.api.replay.ReplayManager {

    @Getter
    private final MongoCollection<eu.mcone.gameapi.api.replay.Replay> replayCollection = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), new LocationCodecProvider(), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("replay", eu.mcone.gameapi.api.replay.Replay.class);

    private final List<ReplayRecord> replays;
    private WorldDownloader worldDownloader;

    private ReplayViewManager replayViewManager;
    @Getter
    private final CodecRegistry codecRegistry;

    public ReplayManager() {
        replays = new ArrayList<>();

        if (GamePlugin.getGamePlugin().hasOption(Option.DOWNLOAD_REPLAY_WORLDS)) {
            GamePlugin.getGamePlugin().sendConsoleMessage("§aStarting world downloader...");
            worldDownloader = new WorldDownloader();
            worldDownloader.runDownloader();
        }

        if (GamePlugin.getGamePlugin().hasOption(Option.USE_REPLAY_VIEW_MANAGER)) {
            replayViewManager = new ReplayViewManager();
        }

        codecRegistry = CoreSystem.getInstance().createCodecRegistry(true);
        registerCodecs();
        registerCommand();
        registerReportMethod();
    }

    private void registerCodecs() {
        //Default Codecs
        codecRegistry.registerCodec((byte) 1, PlayerMoveEventCodec.class, PlayerMoveEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 2, PlayInUseBlockCodec.class, PlayerInteractEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 3, PlayInUseItemCodec.class, PlayerInteractEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 4, ItemSwitchEventCodec.class, PlayerItemHeldEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 5, PlayInEntityActionCodec.class, PacketPlayInEntityAction.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 6, PlayOutAnimationCodec.class, PacketPlayOutAnimation.class, (byte) 2, PlayerNpc.class);

        //Replay Codecs - Events
        codecRegistry.registerCodec((byte) 7, PlayerJoinReplayEventCodec.class, PlayerJoinReplayEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 8, PlayerQuitReplayEventCodec.class, PlayerQuitReplayEvent.class, (byte) 2, PlayerNpc.class);

        codecRegistry.registerCodec((byte) 9, BlockBreakEventCodec.class, BlockBreakEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 10, BlockPlaceEventCodec.class, BlockPlaceEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 11, PlayerPickupItemEventCodec.class, PlayerPickupItemEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 12, PlayerDropItemEventCodec.class, PlayerDropItemEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 13, PlayerDeathEventCodec.class, PlayerDeathEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 14, EntityRespawnEventCodec.class, PlayerRespawnEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 15, EntityDamageByEntityEventCodec.class, EntityDamageByEntityEvent.class, (byte) 2, PlayerNpc.class);

        codecRegistry.registerCodec((byte) 16, ArmorEquipEventCodec.class, ArmorEquipEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 17, InventoryCloseEventCodec.class, InventoryCloseEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 18, CoreObjectiveCreateEventCodec.class, CoreObjectiveCreateEvent.class, (byte) 4, ReplayPlayer.class);
        codecRegistry.registerCodec((byte) 19, CoreSidebarObjectiveUpdateEventCodec.class, CoreSidebarObjectiveUpdateEvent.class, (byte) 4, ReplayPlayer.class);

        codecRegistry.registerCodec((byte) 20, PlayerInteractEventCodec.class, PlayerInteractEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 21, PlayerItemConsumeEventCodec.class, PlayerItemConsumeEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 22, ProjectileLaunchEventCodec.class, ProjectileLaunchEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 23, EntityExplodeEventCodec.class, EntityExplodeEvent.class, (byte) 5, SyncServerRunner.class);
        codecRegistry.registerCodec((byte) 24, StatsChangeEventCodec.class, StatsChangeEvent.class, (byte) 4, ReplayPlayer.class);
        codecRegistry.registerCodec((byte) 25, EntityShootBowEventCodec.class, EntityShootBowEvent.class, (byte) 4, ReplayPlayer.class);

        //Replay Codecs - Packets
        codecRegistry.registerCodec((byte) 26, PlayOutUpdateHealthCodec.class, PacketPlayOutUpdateHealth.class, (byte) 4, ReplayPlayer.class);
        codecRegistry.registerCodec((byte) 27, PacketPlayOutNamedSoundEffectCodec.class, PacketPlayOutNamedSoundEffect.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 28, PacketPlayOutEntityEffectCodec.class, PacketPlayOutEntityEffect.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 29, PacketPlayInBlockDigCodec.class, PacketPlayInBlockDig.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 30, PacketPlayOutSpawnEntityCodec.class, PacketPlayOutSpawnEntity.class, (byte) 3, PlayerRunner.class);
    }

    private void registerCommand() {
        GamePlugin.getGamePlugin().registerCommands(
                new ReplayCMD()
        );
    }

    private void registerReportMethod() {
        CoreSystem.getInstance().getOverwatch().getReportManager().setReportMethod((manager, reporter, reported, reportReason) -> {
            CorePlayer corePlayer = CoreSystem.getInstance().getCorePlayer(reporter);
            if (replays.size() > 0) {
                return new Report(reported.getUniqueId(), reporter.getUniqueId(), reportReason, corePlayer.getTrust().getGroup().getTrustPoints(), replays.get(0).getID());
            } else {
                return new Report(reported.getUniqueId(), reporter.getUniqueId(), reportReason, corePlayer.getTrust().getGroup().getTrustPoints());
            }
        });
    }

    public eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader() {
        return worldDownloader;
    }

    public eu.mcone.gameapi.api.replay.ReplayViewManager getReplayViewManager() {
        try {
            if (GamePlugin.getGamePlugin().hasOption(Option.USE_REPLAY_VIEW_MANAGER)) {
                return replayViewManager;
            } else {
                throw new GameModuleNotActiveException("The game module ReplayViewManager isn`t active!");
            }
        } catch (GameModuleNotActiveException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ReplayRecord createReplay(String gameID) {
        ReplayRecord replayRecord = new eu.mcone.gameapi.replay.ReplayRecord(gameID);
        replays.add(replayRecord);
        return replayRecord;
    }

    public ReplayRecord createReplay(String ID, String gameID) {
        ReplayRecord replayRecord = new eu.mcone.gameapi.replay.ReplayRecord(ID, gameID);
        replays.add(replayRecord);
        return replayRecord;
    }

    @Override
    public void saveReplay(final ReplayRecord replayRecord) {
        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getGamePlugin(), () -> {
            Replay replay = new Replay(replayRecord);
            ChunkHandler handler = new ChunkHandler(replay, replayRecord.getRecorder().getChunks());
            handler.save();
            replayCollection.insertOne(replay);
        });
    }

    @Override
    public boolean deleteReplay(final String ID) {
        return replayCollection.deleteOne(eq("iD", ID)).getDeletedCount() > 0;
    }

    /**
     * Gets the live version of the replay session from the database
     *
     * @param replayID Unique ID
     * @return ReplaySession interface
     */
    @Override
    public eu.mcone.gameapi.api.replay.Replay getReplay(final String replayID) {
        try {
            eu.mcone.gameapi.api.replay.Replay replay = replayCollection.find(eq("iD", replayID)).first();

            if (replay != null) {
                download(replay.getWorld());
                return replay;
            } else {
                throw new ReplaySessionNotFoundException();
            }
        } catch (ReplaySessionNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks if the specified sessionID already exists
     *
     * @param id Unique ID
     * @return boolean
     */
    @Override
    public boolean existsReplay(final String id) {
        return replayCollection.find(eq("iD", id)).first() != null;
    }

    @Override
    public long getReplaySize() {
        return replayCollection.countDocuments();
    }

    @Override
    public long countReplaysForGamemodeAndPlayer(Player player, Gamemode gamemode) {
        return replayCollection.countDocuments(and(exists("replayPlayers." + player.getUniqueId()), eq("gamemode", gamemode.toString())));
    }

    @Override
    public long countReplaysForGamemode(Gamemode gamemode) {
        return replayCollection.countDocuments(eq("gamemode", gamemode.toString()));
    }

    @Override
    public long countReplaysForPlayer(final Player player) {
        return replayCollection.countDocuments(exists("replayPlayers." + player.getUniqueId()));
    }

    @Override
    public FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(Gamemode gamemode) {
        return replayCollection.find(eq("gamemode", gamemode.toString()));
    }

    @Override
    public FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(Gamemode gamemode, final int skip, final int limit) {
        return replayCollection.find(eq("gamemode", gamemode.toString())).skip(skip).limit(limit);
    }

    @Override
    public FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(final int skip, final int limit) {
        return replayCollection.find().skip(skip).limit(limit);
    }

    /**
     * Returns a List of Replays for the specified UUID (Player) only from the local cache.
     *
     * @param uuid Unique UUID
     * @return List of ReplaySessions
     */
    @Override
    public FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplaysForPlayer(final UUID uuid, final int skip, final int limit) {
        return replayCollection.find(exists("replayPlayers." + uuid)).skip(skip).limit(limit);
    }

    @Override
    public FindIterable<eu.mcone.gameapi.api.replay.Replay> getReplays(Player player, Gamemode gamemode, final int skip, final int limit) {
        return replayCollection.find(and(exists("replayPlayers." + player.getUniqueId()), eq("gamemode", gamemode.toString()))).skip(skip).limit(limit);
    }

    /**
     * Returns a list of all replay sessions in the database
     *
     * @return Replay
     */
    @Override
    public List<ReplayRecord> getRecording() {
        return replays;
    }

    private void download(String world) {
        if (!CoreSystem.getInstance().getWorldManager().existWorld(world)) {
            if (worldDownloader != null) {
                worldDownloader.getDownloaded().add(world);
            }

            CoreSystem.getInstance().getWorldManager().download(world, (succeeded) -> {
                if (succeeded) {
                    CoreWorld downloadedWorld = CoreSystem.getInstance().getWorldManager().getWorld(world);
                    downloadedWorld.setLoadOnStartup(false);
                    downloadedWorld.save();
                }
            });
        }
    }
}
