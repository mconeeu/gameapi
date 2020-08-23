package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.utils.SkipUnit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public interface AsyncServerRunner {

    ReplayContainer getContainer();

    AtomicInteger getCurrentTick();

    boolean isSync();

    boolean isForward();

    void setForward(boolean forward);

    boolean isPlaying();

    void setPlaying(boolean playing);

    ReplaySpeed getSpeed();

    void setSpeed(ReplaySpeed speed);

    void play();

    void stop();

    void restart();

    void skip(SkipUnit unit, int ticks);

    Collection<Player> getViewers();
}
