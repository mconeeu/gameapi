package eu.mcone.gameapi.api.replay.utils;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface Replay {

    PlayerNpc getNpc();

    Map<String, List<PacketWrapper>> getPackets();

    ReplayPlayer getPlayer();

    Location getLocation();

    void play();

    void stopPlaying();

    void startPlaying();

    void restart();

    void backward();

    void forward();

    void stop();

    int getCurrentTick();

    boolean isPlaying();

    void addWatcher(Player... players);

    void removeWatcher(Player... players);

    void removeWatcher(Player player);

    void setSpeed(double speed);
}
