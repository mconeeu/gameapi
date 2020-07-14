package eu.mcone.gameapi.replay.session;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.event.StatsChangeEvent;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreObjectiveCreateEvent;
import eu.mcone.coresystem.api.bukkit.event.objectiv.CoreSidebarObjectiveUpdateEvent;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.npc.capture.codecs.*;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplayEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplayEvent;
import eu.mcone.gameapi.api.replay.exception.ReplayPlayerAlreadyExistsException;
import eu.mcone.gameapi.api.replay.packets.player.*;
import eu.mcone.gameapi.api.replay.packets.player.block.BlockBreakEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.BlockPlaceEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.PacketPlayInBlockDigCodec;
import eu.mcone.gameapi.api.replay.packets.player.block.PlayerInteractEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.inventory.InventoryCloseEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.objective.CoreObjectiveCreateEventCodec;
import eu.mcone.gameapi.api.replay.packets.player.objective.CoreSidebarObjectiveUpdateEventCodec;
import eu.mcone.gameapi.api.replay.packets.server.EntityExplodeEventCodec;
import eu.mcone.gameapi.api.utils.IDUtils;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.utils.ReplayRecorder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
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

import java.util.*;

@Getter
@Setter
public class ReplayRecord implements eu.mcone.gameapi.api.replay.session.ReplayRecord {

    @Getter
    private String ID;
    private Gamemode gamemode;
    private List<Option> options;

    private Map<String, eu.mcone.gameapi.api.replay.player.ReplayPlayer> players;
    private CodecRegistry codecRegistry;
    private ReplayRecorder recorder;

    public ReplayRecord(final String ID, final Gamemode gamemode, Option... options) {
        this.ID = ID;
        this.gamemode = gamemode;
        this.options = Arrays.asList(options);

        players = new HashMap<>();
        codecRegistry = CoreSystem.getInstance().createCodecRegistry(true);
        //Default Codecs
        codecRegistry.registerCodec(PacketPlayInEntityAction.class, PlayInEntityActionCodec.class);
        codecRegistry.registerCodec(PlayerItemHeldEvent.class, ItemSwitchEventCodec.class);
        codecRegistry.registerCodec(PacketPlayInUseEntity.class, PlayInUseCodec.class);
        codecRegistry.registerCodec(PacketPlayOutAnimation.class, PlayOutAnimationCodec.class);
        codecRegistry.registerCodec(PlayerMoveEvent.class, PlayerMoveEventCodec.class);

        //Replay Codecs - Events
        codecRegistry.registerCodec(StatsChangeEvent.class, StatsChangeEventCodec.class);
        codecRegistry.registerCodec(ProjectileLaunchEvent.class, ProjectileLaunchEventCodec.class);
        codecRegistry.registerCodec(PlayerQuitReplayEvent.class, PlayerQuitReplayEventCodec.class);
        codecRegistry.registerCodec(PlayerJoinReplayEvent.class, PlayerJoinReplayEventCodec.class);
        codecRegistry.registerCodec(PlayerItemConsumeEvent.class, PlayerItemConsumeEventCodec.class);
        codecRegistry.registerCodec(PlayerDropItemEvent.class, PlayerDropItemEventCodec.class);
        codecRegistry.registerCodec(PlayerDeathEvent.class, PlayerDeathEventCodec.class);
        codecRegistry.registerCodec(EntityDamageByEntityEvent.class, EntityDamageByEntityEventCodec.class);
        codecRegistry.registerCodec(ArmorEquipEvent.class, ArmorEquipEventCodec.class);
        codecRegistry.registerCodec(CoreSidebarObjectiveUpdateEvent.class, CoreSidebarObjectiveUpdateEventCodec.class);
        codecRegistry.registerCodec(CoreObjectiveCreateEvent.class, CoreObjectiveCreateEventCodec.class);
        codecRegistry.registerCodec(InventoryCloseEvent.class, InventoryCloseEventCodec.class);
        codecRegistry.registerCodec(PlayerInteractEvent.class, PlayerInteractEventCodec.class);
        codecRegistry.registerCodec(BlockPlaceEvent.class, BlockPlaceEventCodec.class);
        codecRegistry.registerCodec(BlockBreakEvent.class, BlockBreakEventCodec.class);
        codecRegistry.registerCodec(EntityExplodeEvent.class, EntityExplodeEventCodec.class);

        //Replay Codecs - Packets
        codecRegistry.registerCodec(PacketPlayOutUpdateHealth.class, PlayOutUpdateHealthCodec.class);
        codecRegistry.registerCodec(PacketPlayOutNamedSoundEffect.class, PacketPlayOutNamedSoundEffectCodec.class);
        codecRegistry.registerCodec(PacketPlayOutEntityEffect.class, PacketPlayOutNamedSoundEffectCodec.class);
        codecRegistry.registerCodec(PacketPlayInBlockDig.class, PacketPlayInBlockDigCodec.class);
        codecRegistry.registerCodec(PacketPlayOutSpawnEntity.class, PacketPlayOutSpawnEntityCodec.class);

        recorder = new ReplayRecorder(this, codecRegistry);
    }

    public ReplayRecord(final Gamemode gamemode, final Option... options) {
        this.ID = IDUtils.generateID();
        this.gamemode = gamemode;
        this.options = Arrays.asList(options);

        players = new HashMap<>();
        codecRegistry = CoreSystem.getInstance().createCodecRegistry(true);
        recorder = new ReplayRecorder(this, codecRegistry);
    }

    public void recordSession() {
        boolean succeed = true;
        if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(recorder.getWorld())) {
            succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(recorder.getWorld()));
        }

        if (succeed) {
            recorder.record();

            //Adds the world entity spawn packet
            for (eu.mcone.gameapi.api.replay.player.ReplayPlayer player : players.values()) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinReplayEvent(Bukkit.getPlayer(player.getUuid())));
            }
        } else {
            throw new IllegalStateException("[REPLAY] Could not upload World " + recorder.getWorld() + " to database!");
        }
    }

    public void save() {
        recorder.stop();
        GamePlugin.getGamePlugin().getReplayManager().saveReplay(this);
    }

    public void addPlayer(final Player player) {
        try {
            if (!players.containsKey(player.getUniqueId().toString())) {
                players.put(player.getUniqueId().toString(), new ReplayPlayer(player));
            } else {
                throw new ReplayPlayerAlreadyExistsException();
            }
        } catch (ReplayPlayerAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(final Player player) {
        try {
            if (players.containsKey(player.getUniqueId().toString())) {
                players.remove(player.getUniqueId().toString());
                Bukkit.getServer().getPluginManager().callEvent(new PlayerQuitReplayEvent(player));
            } else {
                throw new NullPointerException("ReplayPlayer " + player.getName() + " doesnt exists!");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid) {
        return players.getOrDefault(uuid.toString(), null);
    }

    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player) {
        return getReplayPlayer(player.getUniqueId());
    }

    public Collection<eu.mcone.gameapi.api.replay.player.ReplayPlayer> getPlayersAsObject() {
        return new ArrayList<>(players.values());
    }

    public boolean existsReplayPlayer(final UUID uuid) {
        return players.containsKey(uuid.toString());
    }

    public boolean existsReplayPlayer(final Player player) {
        return existsReplayPlayer(player.getUniqueId());
    }
}
