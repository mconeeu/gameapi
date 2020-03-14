package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public interface ReplayRunnerManager {

    boolean isForward();

    boolean isBackward();

    boolean isShowProgress();

    void setShowProgress(boolean progress);

    double getSpeed();

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

    void setSpeed(double speed);

    HashSet<Player> getWatchers();

    Collection<PlayerRunner> getSingleRunners();

    PlayerRunner createSingleRunner(final ReplayPlayer replayPlayer);

    void openSpectatorInventory(Player player);
}
