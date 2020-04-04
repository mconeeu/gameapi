package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public interface ReplayRunnerManager {

    String getRunnerID();

    boolean isForward();

    boolean isBackward();

    boolean isShowProgress();

    void setShowProgress(boolean progress);

    ReplaySpeed getReplaySpeed();

    void play();

    void stopPlaying();

    void startPlaying();

    void restart();

    void backward();

    void forward();

    void stop();

    AtomicInteger getCurrentTick();

    boolean isPlaying();

    void addWatcher(Player... players);

    void removeWatcher(Player... players);

    void setSpeed(ReplaySpeed speed);

    void skip(int ticks);

    HashSet<Player> getWatchers();

    Collection<PlayerRunner> getSingleRunners();

    PlayerRunner createSingleRunner(final ReplayPlayer replayPlayer);

    void openSpectatorInventory(Player player);
}
