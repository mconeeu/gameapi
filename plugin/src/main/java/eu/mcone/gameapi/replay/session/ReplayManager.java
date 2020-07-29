package eu.mcone.gameapi.replay.session;

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
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplayEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplayEvent;
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
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import eu.mcone.gameapi.command.ReplayCMD;
import eu.mcone.gameapi.replay.runner.PlayerRunner;
import eu.mcone.gameapi.replay.runner.ServerRunner;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ReplayManager implements eu.mcone.gameapi.api.replay.session.ReplayManager {

    @Getter
    private final MongoCollection<Replay> replayCollection = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new UuidCodecProvider(UuidRepresentation.JAVA_LEGACY), new LocationCodecProvider(), PojoCodecProvider.builder().conventions(Conventions.DEFAULT_CONVENTIONS).automatic(true).build()))
    ).getCollection("replay", Replay.class);

    private final List<ReplayRecord> replays;
    private WorldDownloader worldDownloader;

    @Getter
    private final CodecRegistry codecRegistry;

    public ReplayManager(final Option... options) {
        replays = new ArrayList<>();

        List<Option> optionsList = Arrays.asList(options);
        if (optionsList.contains(Option.REPLAY_MANAGER_USE_WORLD_DOWNLOADER)) {
            GameAPIPlugin.getSystem().sendConsoleMessage("§aStarting world downloader...");
            worldDownloader = new WorldDownloader();
            worldDownloader.runDownloader();
        }

        codecRegistry = CoreSystem.getInstance().createCodecRegistry(true);
        registerCodecs();
    }

    private void registerCodecs() {
        //Default Codecs
        codecRegistry.registerCodec((byte) 1, PlayerMoveEventCodec.class, PlayerMoveEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 2, PlayInUseCodec.class, PlayerInteractEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 3, ItemSwitchEventCodec.class, PlayerItemHeldEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 4, PlayInEntityActionCodec.class, PacketPlayInEntityAction.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 5, PlayOutAnimationCodec.class, PacketPlayOutAnimation.class, (byte) 2, PlayerNpc.class);

        //Replay Codecs - Events
        codecRegistry.registerCodec((byte) 6, PlayerJoinReplayEventCodec.class, PlayerJoinReplayEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 7, PlayerQuitReplayEventCodec.class, PlayerQuitReplayEvent.class, (byte) 2, PlayerNpc.class);

        codecRegistry.registerCodec((byte) 8, BlockBreakEventCodec.class, BlockBreakEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 9, BlockPlaceEventCodec.class, BlockPlaceEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 10, PlayerPickupItemEventCodec.class, PlayerPickupItemEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 11, PlayerDropItemEventCodec.class, PlayerDropItemEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 12, PlayerDeathEventCodec.class, PlayerDeathEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 13, EntityRespawnEventCodec.class, PlayerRespawnEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 14, EntityDamageByEntityEventCodec.class, EntityDamageByEntityEvent.class, (byte) 2, PlayerNpc.class);

        codecRegistry.registerCodec((byte) 15, ArmorEquipEventCodec.class, ArmorEquipEvent.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 16, InventoryCloseEventCodec.class, InventoryCloseEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 17, CoreObjectiveCreateEventCodec.class, CoreObjectiveCreateEvent.class, (byte) 4, eu.mcone.gameapi.replay.player.ReplayPlayer.class);
        codecRegistry.registerCodec((byte) 18, CoreSidebarObjectiveUpdateEventCodec.class, CoreSidebarObjectiveUpdateEvent.class, (byte) 4, eu.mcone.gameapi.replay.player.ReplayPlayer.class);

        codecRegistry.registerCodec((byte) 19, PlayerInteractEventCodec.class, PlayerInteractEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 20, PlayerItemConsumeEventCodec.class, PlayerItemConsumeEvent.class, (byte) 3, PlayerRunner.class);

        codecRegistry.registerCodec((byte) 21, ProjectileLaunchEventCodec.class, ProjectileLaunchEvent.class, (byte) 2, PlayerNpc.class);
        codecRegistry.registerCodec((byte) 22, EntityExplodeEventCodec.class, EntityExplodeEvent.class, (byte) 5, ServerRunner.class);
        codecRegistry.registerCodec((byte) 23, StatsChangeEventCodec.class, StatsChangeEvent.class, (byte) 4, eu.mcone.gameapi.replay.player.ReplayPlayer.class);

        //Replay Codecs - Packets
        codecRegistry.registerCodec((byte) 24, PlayOutUpdateHealthCodec.class, PacketPlayOutUpdateHealth.class, (byte) 4, eu.mcone.gameapi.replay.player.ReplayPlayer.class);
        codecRegistry.registerCodec((byte) 25, PacketPlayOutNamedSoundEffectCodec.class, PacketPlayOutNamedSoundEffect.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 26, PacketPlayOutEntityEffectCodec.class, PacketPlayOutEntityEffect.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 27, PacketPlayInBlockDigCodec.class, PacketPlayInBlockDig.class, (byte) 3, PlayerRunner.class);
        codecRegistry.registerCodec((byte) 28, PacketPlayOutSpawnEntityCodec.class, PacketPlayOutSpawnEntity.class, (byte) 3, PlayerRunner.class);
    }

    public void registerCommand() {
        GamePlugin.getGamePlugin().registerCommands(
                new ReplayCMD()
        );
    }

    public eu.mcone.gameapi.api.replay.world.WorldDownloader getWorldDownloader() {
        return worldDownloader;
    }

    public ReplayRecord createReplay(final String ID, final Gamemode gamemode, Option... options) {
        eu.mcone.gameapi.replay.session.ReplayRecord replayRecord = new eu.mcone.gameapi.replay.session.ReplayRecord(ID, gamemode, options);
        replays.add(replayRecord);
        return replayRecord;
    }

    public ReplayRecord createReplay(final Gamemode gamemode, Option... options) {
        eu.mcone.gameapi.replay.session.ReplayRecord replayRecord = new eu.mcone.gameapi.replay.session.ReplayRecord(gamemode, options);
        replays.add(replayRecord);
        return replayRecord;
    }

    @Override
    public void saveReplay(final ReplayRecord replayRecord) {
        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getGamePlugin(), () -> {
            Replay replay = new Replay(replayRecord);
            replay.getChunkHandler().save();
            replayCollection.insertOne(replay);
        });
    }

    @Override
    public boolean deleteSession(final String sessionID) {
        return replayCollection.deleteOne(eq("ID", sessionID)).getDeletedCount() > 0;
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
            Replay replay = replayCollection.find(eq("ID", replayID)).first();

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
     * Returns a List of Replays for the specified UUID (Player) only from the local cache.
     *
     * @param uuid Unique UUID
     * @return List of ReplaySessions
     */
    @Override
    public List<eu.mcone.gameapi.api.replay.session.Replay> getReplaysForPlayer(final UUID uuid, final int row, final int limit) {
        List<eu.mcone.gameapi.api.replay.session.Replay> replays = new ArrayList<>();
        for (eu.mcone.gameapi.api.replay.session.Replay replay : replayCollection.find(in("replayPlayers", uuid)).skip(row * limit).limit(limit)) {
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
     * @param id Unique ID
     * @return boolean
     */
    @Override
    public boolean existsReplay(final String id) {
        return replayCollection.find(eq("ID", id)).first() != null;
    }

    @Override
    public long getReplaySize() {
        return replayCollection.countDocuments();
    }

    @Override
    public long getPlayerReplaySize(final Player player) {
        return replayCollection.countDocuments(in("replayPlayers", player.getUniqueId()));
    }

    @Override
    public List<eu.mcone.gameapi.api.replay.session.Replay> getReplays(final int row, final int limit) {
        List<eu.mcone.gameapi.api.replay.session.Replay> sessions = new ArrayList<>();
        replayCollection.find().skip(row * limit).limit(limit).iterator().forEachRemaining(sessions::add);
        return sessions;
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

            CoreSystem.getInstance().getWorldManager().download(world);
            CoreWorld downloadedWorld = CoreSystem.getInstance().getWorldManager().getWorld(world);
            downloadedWorld.setLoadOnStartup(false);
            downloadedWorld.save();
        }
    }
}
