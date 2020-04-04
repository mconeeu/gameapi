package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.event.*;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import eu.mcone.gameapi.replay.inventory.ReplaySpectatorInventory;
import eu.mcone.gameapi.replay.listener.NPCInteractListener;
import eu.mcone.gameapi.replay.npc.NpcUtils;
import eu.mcone.gameapi.replay.session.ReplaySession;
import eu.mcone.gameapi.api.utils.IDUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class ReplayRunnerManager implements eu.mcone.gameapi.api.replay.runner.ReplayRunnerManager {

    private final String runnerID;

    private boolean playing = false;
    private AtomicInteger currentTick = new AtomicInteger();
    private boolean forward = true;
    private boolean backward = false;
    @Setter
    private boolean showProgress = true;
    private ReplaySpeed replaySpeed = null;
    private int skipped;

    private ReplaySession session;
    private HashSet<Player> watchers;
    private ServerRunner serverRunner;
    private HashMap<UUID, eu.mcone.gameapi.api.replay.runner.PlayerRunner> singleReplays;
    private HashMap<UUID, PlayerRunner> replays;

    private BukkitTask progressTask;

    private NPCInteractListener npcListener;

    public ReplayRunnerManager(ReplaySession session) {
        this.session = session;
        runnerID = IDUtils.generateID();
        watchers = new HashSet<>();
        singleReplays = new HashMap<>();
        serverRunner = new ServerRunner(this);
        replays = new HashMap<>();
    }

    public void addWatcher(final Player... players) {
        //Check if the loading is to slow
        session.getChunkHandler().preLoad();

        replays.forEach((key, value) -> value.addWatcher(players));

        for (Player player : players) {
            Bukkit.getPluginManager().callEvent(new WatcherJoinReplayEvent(player, watchers.toArray(new Player[0]), session));

            watchers.add(player);
            serverRunner.addWatcher(player);

            for (PlayerRunner runner : replays.values()) {
                runner.addWatcher(player);
                runner.getPlayer().getNpc().toggleVisibility(player, false);
            }
        }
    }

    public void removeWatcher(final Player... players) {
        replays.forEach((key, value) -> value.removeWatcher(players));

        for (Player player : players) {
            watchers.remove(player);
            serverRunner.removeWatcher(player);

            for (PlayerRunner runner : replays.values()) {
                runner.removeWatcher(player);
                runner.getPlayer().getNpc().toggleVisibility(player, true);
            }

            Bukkit.getPluginManager().callEvent(new WatcherQuitReplayEvent(player, watchers.toArray(new Player[0]), session));

            if (playing && watchers.isEmpty()) {
                stop();
            }
        }
    }

    private void progress() {
        progressTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getInstance(), () -> {
            for (Player watcher : watchers) {
                if (playing && showProgress) {
                    if (currentTick.get() == session.getInfo().getLastTick()) {
                        System.out.println("STOP");
                        stop();
                        Bukkit.getPluginManager().callEvent(new ReplayStopEvent(session, session.getInfo().getLastTick()));
                    } else {
                        int repeat;
                        if (replaySpeed != null && skipped == replaySpeed.getWait()) {
                            repeat = (replaySpeed.isAdd() ? 2 : 0);
                            skipped = 0;
                        } else {
                            repeat = 1;
                        }

                        for (int i = 0; i < repeat; i++) {
                            int tick;
                            if (forward) {
                                tick = currentTick.getAndIncrement();
                            } else {
                                tick = currentTick.getAndDecrement();
                            }

                            CoreSystem.getInstance().createActionBar().message(ReplayInformationInventory.getLength(tick)).send(watcher);
                        }
                    }
                } else {
                    CoreSystem.getInstance().createActionBar().message("§7Paussiert");
                }
            }
        }, 1L, 1L);
    }

    public void restart() {
        if (this.playing) {
            this.replaySpeed = null;
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

            for (eu.mcone.gameapi.replay.player.ReplayPlayer player : session.getPlayersAsObject()) {
                if (!(replays.containsKey(player.getUuid()) || singleReplays.containsKey(player.getUuid()))) {
                    player.setNpc(NpcUtils.constructNpcForPlayer(player, runnerID));
                    PlayerRunner runner = new PlayerRunner(player, this);
                    runner.addWatcher(watchers.toArray(new Player[0]));
                    runner.play();
                    replays.put(player.getUuid(), runner);
                }
            }

            serverRunner.play();
            progress();
            npcListener = new NPCInteractListener(this);
            Bukkit.getPluginManager().callEvent(new ReplayStartEvent(session));
        }
    }

    public void stop() {
        if (playing && replays.size() > 0) {
            for (PlayerRunner runner : replays.values()) {
                runner.stop();
                runner.setReplaySpeed(null);
            }

            serverRunner.stop();
            serverRunner.setReplaySpeed(null);

            forward = true;
            replaySpeed = null;
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

    public void setSpeed(ReplaySpeed speed) {
        if (playing) {
            this.replaySpeed = speed;

            for (PlayerRunner runner : replays.values()) {
                runner.setReplaySpeed(speed);
            }

            serverRunner.setReplaySpeed(speed);
        }
    }

    public void skip(int ticks) {
        if (playing) {
            for (PlayerRunner runner : replays.values()) {
                runner.skip(ticks);
            }

            int newTick = currentTick.get() + ticks;
            if (newTick > 0) {
                currentTick.set(newTick);
            } else {
                currentTick.set(0);
            }

            serverRunner.skip(ticks);
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
        if (watchers.contains(player))
            new ReplaySpectatorInventory(session, player);
    }
}
