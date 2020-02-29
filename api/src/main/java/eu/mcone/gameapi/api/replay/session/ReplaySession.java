package eu.mcone.gameapi.api.replay.session;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ReplaySession {

    String getID();

    ServerInfo getInfo();

    HashMap<String, List<PacketWrapper>> getServerPackets();

    HashMap<String, List<PacketWrapper>> getWorldPackets();

    void recordSession();

    void saveSession();

    void addPlayer(final Player player);

    void removePlayer(final Player player);

    ReplayPlayer getReplayPlayer(final UUID uuid);

    ReplayPlayer getReplayPlayer(final Player player);

    Collection<ReplayPlayer> getPlayers();

    boolean existsReplayPlayer(final UUID uuid);

    boolean existsReplayPlayer(final Player player);

    void openInformationInventory(Player player);

    void openSpectatorInventory(Player player);

    interface ServerInfo {

        long getStarted();

        void setStarted(long timestamp);

        long getStopped();

        void setStopped(long timestamp);

        int getTeams();

        void setTeams(int teams);

        String getWinnerTeam();

        void setWinnerTeam(String winnerTeam);

        String getWorld();

        void setWorld(String world);

        double getLength();

        void setLength(double length);
    }
}
