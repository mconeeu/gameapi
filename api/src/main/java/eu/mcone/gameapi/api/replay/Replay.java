package eu.mcone.gameapi.api.replay;

import eu.mcone.gameapi.api.game.GameHistory;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@BsonDiscriminator
public interface Replay {

    String getID();

    String getWorld();

    int getLastTick();

    Map<String, List<MessageWrapper>> getMessages();

    GameHistory getGameHistory();

    ReplayContainer createContainer();

    Collection<ReplayContainer> getContainers();

    void removeContainer(UUID uuid);

    ReplayContainer getContainer(UUID uuid);

    ReplayContainer getContainer(Player player);

    eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final UUID uuid);

    eu.mcone.gameapi.api.replay.player.ReplayPlayer getReplayPlayer(final Player player);

    Collection<ReplayPlayer> getPlayers();

    boolean existsReplayPlayer(final UUID uuid);

    boolean existsReplayPlayer(final Player player);

    void openInformationInventory(Player player);

}
