package eu.mcone.gameapi.api.replay.container;

import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.Replay;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunkHandler;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.AsyncPlayerRunner;
import eu.mcone.gameapi.api.replay.runner.ReplayRunner;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.api.replay.utils.SkipUnit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public interface ReplayContainer {

    Replay getReplay();

    UUID getContainerUUID();

    int getTick();

    boolean isPlaying();

    boolean isForward();

    boolean isShowProgress();

    ReplaySpeed getSpeed();

    HashSet<Player> getViewers();

    HashMap<Integer, Integer> getEntities();

    ReplayChunkHandler getChunkHandler();

    void addViewers(final Player... players);

    void removeViewers(final Player... players);

    void play();

    void restart();

    void stop();

    void playing(boolean playing);

    void forward(boolean forward);

    void nextSpeed();

    void setSpeed(ReplaySpeed speed);

    void skip(SkipUnit unit, int amount);

    void addIdling(ReplayRunner replayRunner);

    void removeIdling(ReplayRunner replayRunner);

    boolean isInCamera(Player player);

    void joinCamera(Player player, PlayerNpc playerNpc);

    void leaveCamera(Player player);

    AsyncPlayerRunner createAsyncRunner(final ReplayPlayer player);

    AsyncPlayerRunner getAsyncRunner(ReplayPlayer player);

    void invite(final Player sender, final Player target);

    void openSpectatorInventory(Player player);

    void showProgress(boolean show);
}
