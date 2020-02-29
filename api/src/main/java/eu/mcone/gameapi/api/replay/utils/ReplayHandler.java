package eu.mcone.gameapi.api.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.event.ReplayEndEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.api.utils.ReplayServer;
import eu.mcone.gameapi.api.utils.ReplayWorld;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplayHandler {

    @Getter
    private final ReplaySession session;
    @Getter
    private final HashSet<Player> watchers;
    private final ReplayWorld worldPlayback;
    private final ReplayServer replayServer;

    @Getter
    private boolean playing = false;
    @Getter
    private boolean inverted = false;
    @Getter
    private AtomicInteger progress;
    private double currentSpeed = 1;
    private int lastTick = 0;

    private BukkitTask progressTask;

    public ReplayHandler(final ReplaySession session) {
        this.session = session;
        this.watchers = new HashSet<>();
        worldPlayback = new ReplayWorld(session);
        replayServer = new ReplayServer(session);
        progress = new AtomicInteger();

        for (ReplayPlayer player : session.getPlayers()) {

            int max = 0;
            for (Map.Entry<String, List<PacketWrapper>> entry : player.getPackets().entrySet()) {
                int i = Integer.valueOf(entry.getKey());

                if (i > max) {
                    max = i;
                }
            }

            if (max > lastTick) {
                lastTick = max;
                System.out.println(lastTick);
            }
        }
    }

    private void progress() {
        progressTask = Bukkit.getScheduler().runTaskTimerAsynchronously(GameAPI.getInstance(), () -> {
            for (Player watcher : watchers) {
                if (playing) {
                    if (progress.get() == lastTick) {
                        stopReplay();
                        Bukkit.getPluginManager().callEvent(new ReplayEndEvent(session, lastTick));
                    } else {
                        CoreSystem.getInstance().createActionBar().message("§7" + progress.getAndIncrement() / 60 + " Minuten").send(watcher);
                    }
                } else {
                    CoreSystem.getInstance().createActionBar().message("§7Paussiert");
                }
            }
        }, (long) (1L * currentSpeed), (long) (1L * currentSpeed));
    }

    public void startReplay() {
        System.out.println("Call start replay");
        System.out.println("Watcher size: " + watchers.size());
        if (!playing && watchers.size() > 0) {
            playing = true;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().play();
                System.out.println("Start replay for player " + player.getData().getName());
            }

            progress();
            worldPlayback.play();
            replayServer.play();
        }
    }

    public void stopReplay() {
        if (playing && watchers.size() > 0) {
            playing = false;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().stop();
                System.out.println("Stop replay for player " + player.getData().getName());
            }

            progressTask.cancel();
            worldPlayback.stop();
            replayServer.stop();
        }
    }

    public void startPlaying() {
        System.out.println("Call start playing replay");
        if (!playing) {
            playing = true;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().startPlaying();
            }

            worldPlayback.startPlaying();
            replayServer.startPlaying();
        }
    }

    //Stops the replay
    public void stopPlaying() {
        if (playing) {
            playing = false;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().stopPlaying();
            }

            worldPlayback.stopPlaying();
            replayServer.stopPlaying();
        }
    }

    //Pauses the current replay
    public void holdReplay() {
        if (playing) {
            playing = false;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().stopPlaying();
            }
        }
    }

    public void forward() {
        if (playing && inverted) {
            inverted = false;

            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().forward();
            }

            worldPlayback.forward();
            replayServer.forward();
        }
    }

    public void backward() {
        if (playing && !inverted) {
            inverted = true;
            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().backward();
            }

            worldPlayback.backward();
            replayServer.backward();
        }
    }

    public void setSpeed(double speed) {
        if (playing) {
            currentSpeed = speed;

            for (ReplayPlayer player : session.getPlayers()) {
                player.getReplay().setSpeed(speed);
            }

            worldPlayback.setSpeed(speed);
            replayServer.setSpeed(speed);
        }
    }

    //TODO: Check if tab works
    public void addWatcher(final Player player) {
        for (ReplayPlayer replayPlayer : session.getPlayers()) {
            replayPlayer.getReplay().addWatcher(player);
            worldPlayback.addWatcher(player);
            replayServer.addWatcher(player);

            System.out.println("Watcher added");
//            replayPlayer.getReplay().getNpc().setVisibleOnTab(true, (Player[]) watchers.toArray());
        }

        watchers.add(player);

        if (!playing) {
            if (watchers.size() < 1) {
                startPlaying();
            }
        }
    }

    public void addWatchers(final Player... players) {
        for (Player player : players) {
            addWatcher(player);
        }
    }

    public void removeWatcher(final Player player) {
        for (ReplayPlayer replayPlayer : session.getPlayers()) {
            replayPlayer.getReplay().removeWatcher(player);
            worldPlayback.removeWatcher(player);
            replayServer.removeWatcher(player);

            replayPlayer.getReplay().getNpc().setVisibleOnTab(true, (Player[]) watchers.toArray());
        }

        watchers.remove(player);

        if (watchers.size() < 1) {
            Bukkit.getPluginManager().callEvent(new ReplayEndEvent(session, progress.get()));
            stopPlaying();
        }
    }

    public void removeWatchers(final Player... players) {
        for (Player player : players) {
            removeWatcher(player);
        }
    }
}
