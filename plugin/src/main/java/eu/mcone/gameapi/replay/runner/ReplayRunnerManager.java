package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.event.*;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.inventory.ReplaySpectatorInventory;
import eu.mcone.gameapi.replay.listener.NPCInteractListener;
import eu.mcone.gameapi.replay.session.ReplaySession;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ReplayRunnerManager implements eu.mcone.gameapi.api.replay.runner.ReplayRunnerManager {

    private boolean playing = false;
    private AtomicInteger currentTick = new AtomicInteger();
    private boolean forward = true;
    private boolean backward = false;
    @Setter
    private boolean showProgress = true;
    private double speed = 1.0D;

    private ReplaySession session;
    private HashSet<Player> watchers;
    private ServerRunner serverRunner;
    private HashMap<UUID, eu.mcone.gameapi.api.replay.runner.PlayerRunner> singleReplays;
    private HashMap<UUID, PlayerRunner> replays;

    private BukkitTask progressTask;

    private NPCInteractListener npcListener;

    public ReplayRunnerManager(ReplaySession session) {
        this.session = session;
        watchers = new HashSet<>();
        singleReplays = new HashMap<>();
        serverRunner = new ServerRunner(session);
        replays = new HashMap<>();
    }

    public void addWatcher(final Player... players) {
        //Check if the loading is to slow
        session.getChunkHandler().preLoad();

        replays.forEach((key, value) -> value.addWatcher(players));

        for (Player player : players) {
            watchers.add(player);
            Bukkit.getPluginManager().callEvent(new WatcherJoinReplayEvent(player, session));
        }
    }

    public void removeWatcher(final Player... players) {
        replays.forEach((key, value) -> value.removeWatcher(players));

        for (Player player : players) {
            watchers.remove(player);
            Bukkit.getPluginManager().callEvent(new WatcherQuitReplayEvent(player, session));

            if (playing && watchers.isEmpty()) {
                stop();
            }
        }
    }

    private void progress() {
        progressTask = Bukkit.getScheduler().runTaskTimerAsynchronously(GameAPI.getInstance(), () -> {
            for (Player watcher : watchers) {
                if (playing && showProgress) {
                    if (currentTick.get() == session.getInfo().getLastTick()) {
                        stop();
                        Bukkit.getPluginManager().callEvent(new ReplayStopEvent(session, session.getInfo().getLastTick()));
                    } else {
                        int tick;
                        if (forward) {
                            tick = currentTick.getAndIncrement();
                        } else {
                            tick = currentTick.getAndDecrement();
                        }

                        CoreSystem.getInstance().createActionBar().message("§7" + tick / 30 + " Sekunden").send(watcher);
                    }
                } else {
                    CoreSystem.getInstance().createActionBar().message("§7Paussiert");
                }
            }
        }, (long) (1L * speed), (long) (1L * speed));
    }

    public void restart() {
        if (this.playing) {
            this.speed = 1;
            this.currentTick = new AtomicInteger(0);
        } else {
            this.forward = true;
            this.backward = false;
            this.play();
        }
    }

    public void play() {
        if (!playing && replays.size() == 0 && watchers.size() > 0) {
            playing = true;

            for (ReplayPlayer player : session.getPlayers()) {
                if (!(replays.containsKey(player.getUuid()) || singleReplays.containsKey(player.getUuid()))) {
                    PlayerRunner runner = new PlayerRunner(player, this);
                    runner.play();
                    replays.put(player.getUuid(), runner);
                }
            }

            serverRunner.play();
            progress();
            npcListener = new NPCInteractListener(session);
            Bukkit.getPluginManager().callEvent(new ReplayStartEvent(session));
        }
    }

    public void stop() {
        if (playing && replays.size() > 0) {
            for (PlayerRunner runner : replays.values()) {
                runner.stop();
            }

            serverRunner.stop();
            serverRunner.setSpeed(1);

            forward = true;
            speed = 1;
            backward = false;
            playing = false;

            npcListener.setUnregister(true);
            replays.clear();
            Bukkit.getScheduler().cancelTask(progressTask.getTaskId());
            Bukkit.getPluginManager().callEvent(new ReplayStopEvent(session, currentTick.get()));
            currentTick.set(0);
        }
    }

    public void startPlaying() {
        if (!playing) {
            playing = true;
            for (PlayerRunner runner : replays.values()) {
                runner.startPlaying();
            }

            serverRunner.stopPlaying();

            Bukkit.getPluginManager().callEvent(new ReplayStartPlayingEvent(session, currentTick.get()));
        }
    }

    public void stopPlaying() {
        if (playing) {
            playing = false;
            for (PlayerRunner runner : replays.values()) {
                runner.stopPlaying();
            }

            serverRunner.stopPlaying();

            Bukkit.getPluginManager().callEvent(new ReplayStopPlayingEvent(session, currentTick.get()));
        }
    }

    public void forward() {
        if (playing && backward) {
            forward = true;
            backward = false;

            for (PlayerRunner runner : replays.values()) {
                runner.forward();
            }

            serverRunner.forward();
        }
    }

    public void backward() {
        if (playing && forward) {
            backward = true;
            forward = false;

            for (PlayerRunner runner : replays.values()) {
                runner.backward();
            }

            serverRunner.backward();
        }
    }

    public void setSpeed(double speed) {
        if (playing) {
            this.speed = speed;

            for (PlayerRunner runner : replays.values()) {
                runner.setSpeed(speed);
            }

            serverRunner.setSpeed(speed);
        }
    }

    public Collection<eu.mcone.gameapi.api.replay.runner.PlayerRunner> getSingleRunners() {
        return singleReplays.values();
    }

    public eu.mcone.gameapi.api.replay.runner.PlayerRunner createSingleRunner(final ReplayPlayer replayPlayer) {
        if (!singleReplays.containsKey(replayPlayer.getUuid())) {
            return new PlayerRunner(replayPlayer, this);
        } else {
            return null;
        }
    }

    public void openSpectatorInventory(Player player) {
        if (singleReplays.containsKey(player.getUniqueId()) || replays.containsKey(player.getUniqueId()))
            new ReplaySpectatorInventory(session, player);
    }
}
