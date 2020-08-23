package eu.mcone.gameapi.replay;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.game.GameHistory;
import eu.mcone.gameapi.api.replay.ReplayRecord;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.event.ReplayWorldUnloadedEvent;
import eu.mcone.gameapi.api.replay.event.container.ReplayContainerCreatedEvent;
import eu.mcone.gameapi.api.replay.event.container.ReplayContainerRemovedEvent;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;

@BsonDiscriminator
public class Replay implements eu.mcone.gameapi.api.replay.Replay, Serializable {

    @Getter
    private final String ID;
    @Getter
    private final String gameID;

    @Getter
    private final String world;
    @Getter
    private final int lastTick;

    //Contains Broadcast messages !
    @Getter
    private final Map<String, List<MessageWrapper>> messages;
    @Getter
    private final Map<String, ReplayPlayer> replayPlayers;

    private final transient Map<UUID, eu.mcone.gameapi.api.replay.container.ReplayContainer> containers;

    public Replay(final ReplayRecord replayRecord) {
        this.ID = replayRecord.getID();
        this.gameID = replayRecord.getGameID();
        this.world = replayRecord.getRecorder().getWorld();
        this.lastTick = replayRecord.getRecorder().getLastTick();

        messages = replayRecord.getGameHistory().getMessages();
        replayPlayers = replayRecord.getPlayers();

        containers = new HashMap<>();
    }

    @BsonCreator
    public Replay(@BsonProperty("iD") String ID, @BsonProperty("gameID") String gameID, @BsonProperty("world") String world, @BsonProperty("lastTick") int lastTick,
                  @BsonProperty("messages") Map<String, List<MessageWrapper>> messages, @BsonProperty("replayPlayers") Map<String, ReplayPlayer> replayPlayers) {
        this.ID = ID;
        this.gameID = gameID;
        this.world = world;
        this.lastTick = lastTick;
        this.messages = messages;
        this.replayPlayers = replayPlayers;

        containers = new HashMap<>();
    }

    public GameHistory getGameHistory() {
        return GamePlugin.getGamePlugin().getGameHistoryManager().getGameHistory(gameID);
    }

    @BsonIgnore
    public ReplayContainer createContainer() {
        ReplayContainer container = new eu.mcone.gameapi.replay.container.ReplayContainer(this);
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
            GamePlugin.getGamePlugin().sendConsoleMessage("§aRemoving Container §f" + uuid);
            for (ReplayPlayer player : getPlayers()) {
                if (player.getNpc() != null) {
                    CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
                }
            }

            containers.remove(uuid);
            Bukkit.getPluginManager().callEvent(new ReplayContainerRemovedEvent(getID(), uuid));

            if (containers.size() == 0) {
                Bukkit.unloadWorld(world, false);
                GamePlugin.getGamePlugin().sendConsoleMessage("§aReplay world unloaded!");
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
            if (container.getViewers().contains(player)) {
                return container;
            }
        }

        return null;
    }

    @BsonIgnore
    public ReplayPlayer getReplayPlayer(final UUID uuid) {
        return replayPlayers.getOrDefault(uuid.toString(), null);
    }

    @BsonIgnore
    public ReplayPlayer getReplayPlayer(final Player player) {
        return getReplayPlayer(player.getUniqueId());
    }

    @BsonIgnore
    public Collection<ReplayPlayer> getPlayers() {
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
