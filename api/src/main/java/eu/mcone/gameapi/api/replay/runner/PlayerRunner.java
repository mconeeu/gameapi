package eu.mcone.gameapi.api.replay.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.utils.SkipUnit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public interface PlayerRunner {

    ReplayContainer getContainer();

    ReplayPlayer getPlayer();

    boolean isSync();

    boolean isBreaking();

    void setBreaking(boolean breaking);

    boolean isForward();

    boolean isPlaying();

    ReplaySpeed getSpeed();

    Collection<Player> getViewers();

    void play();

    void stop();

    void skip(SkipUnit unit, int amount);

    AtomicInteger getCurrentTick();
}
