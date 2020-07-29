package eu.mcone.gameapi.replay.session;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.replay.event.ReplayWorldUnloadedEvent;
import eu.mcone.gameapi.api.replay.event.container.ReplayContainerCreatedEvent;
import eu.mcone.gameapi.api.replay.event.container.ReplayContainerRemovedEvent;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import eu.mcone.gameapi.replay.chunk.ChunkHandler;
import eu.mcone.gameapi.replay.container.ReplayContainer;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;

@Getter
public class Replay implements eu.mcone.gameapi.api.replay.session.Replay, Serializable {

    private final String ID;
    private final long started;
    private final long stopped;
    private final String winnerTeam;
    private final String world;
    private final Gamemode gamemode;
    private final int lastTick;

    //Contains Broadcast messages !
    private final Map<String, List<MessageWrapper>> messages;
    private final Map<String, ReplayPlayer> replayPlayers;

    @BsonIgnore
    private transient final ChunkHandler chunkHandler;
    @BsonIgnore
    private transient final Map<UUID, eu.mcone.gameapi.api.replay.container.ReplayContainer> containers;
    @BsonIgnore
    private transient final Map<UUID, UUID> watchers;

    public Replay(final ReplayRecord replayRecord) {
        this.ID = replayRecord.getID();
        this.started = replayRecord.getRecorder().getStarted();
        this.stopped = replayRecord.getRecorder().getStopped();
        this.winnerTeam = replayRecord.getRecorder().getWinnerTeam();
        this.world = replayRecord.getRecorder().getWorld();
        this.gamemode = replayRecord.getGamemode();
        this.lastTick = replayRecord.getRecorder().getLastTick();

        messages = replayRecord.getRecorder().getMessages();
        replayPlayers = replayRecord.getPlayers();

        chunkHandler = new ChunkHandler(this, replayRecord.getRecorder().getChunks());
        containers = new HashMap<>();
        watchers = new HashMap<>();
    }

    @BsonIgnore
    public ReplayContainer createContainer() {
        ReplayContainer container = new ReplayContainer(this);
        containers.put(container.getContainerUUID(), container);
        Bukkit.getPluginManager().callEvent(new ReplayContainerCreatedEvent(container.getContainerUUID()));
        return container;
    }

    @BsonIgnore
    public Collection<eu.mcone.gameapi.api.replay.container.ReplayContainer> getContainers() {
        return containers.values();
    }

    @BsonIgnore
    public void removeContainer(UUID uuid) {
        if (containers.containsKey(uuid)) {
            GameAPIPlugin.getInstance().sendConsoleMessage("§aRemoving Container §f" + uuid);
            for (ReplayPlayer player : getPlayers()) {
                if (player.getNpc() != null) {
                    CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
                }
            }

            containers.remove(uuid);
            Bukkit.getPluginManager().callEvent(new ReplayContainerRemovedEvent(getID(), uuid));

            if (containers.size() == 0) {
                Bukkit.unloadWorld(world, false);
                GameAPIPlugin.getInstance().sendConsoleMessage("§aReplay world unloaded!");
                Bukkit.getPluginManager().callEvent(new ReplayWorldUnloadedEvent(ID, getWorld()));
            }
        }
    }

    @BsonIgnore
    public eu.mcone.gameapi.api.replay.container.ReplayContainer getContainer(UUID uuid) {
        return containers.getOrDefault(uuid, null);
    }

    @BsonIgnore
    public eu.mcone.gameapi.api.replay.container.ReplayContainer getContainer(Player player) {
        for (eu.mcone.gameapi.api.replay.container.ReplayContainer container : containers.values()) {
            if (container.getWatchers().contains(player)) {
                return container;
            }
        }

        return null;
    }

    @BsonIgnore
    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid) {
        return replayPlayers.getOrDefault(uuid.toString(), null);
    }

    @BsonIgnore
    public eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player) {
        return getReplayPlayer(player.getUniqueId());
    }

    @BsonIgnore
    public Collection<eu.mcone.gameapi.api.replay.player.ReplayPlayer> getPlayers() {
        return new ArrayList<>(replayPlayers.values());
    }

    @BsonIgnore
    public Collection<eu.mcone.gameapi.api.replay.player.ReplayPlayer> getPlayersAsObject() {
        return new ArrayList<>(replayPlayers.values());
    }

    @BsonIgnore
    public boolean existsReplayPlayer(final UUID uuid) {
        return replayPlayers.containsKey(uuid.toString());
    }

    @BsonIgnore
    public boolean existsReplayPlayer(final Player player) {
        return existsReplayPlayer(player.getUniqueId());
    }

    @BsonIgnore
    public void openInformationInventory(Player player) {
        new ReplayInformationInventory(player, this);
    }
}
