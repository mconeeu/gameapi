package eu.mcone.gameapi.replay.container;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.event.runner.*;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.replay.chunk.ChunkHandler;
import eu.mcone.gameapi.replay.inventory.ReplayInformationInventory;
import eu.mcone.gameapi.replay.inventory.ReplaySpectatorInventory;
import eu.mcone.gameapi.replay.listener.NPCInteractListener;
import eu.mcone.gameapi.replay.npc.NpcUtils;
import eu.mcone.gameapi.replay.runner.ServerRunner;
import eu.mcone.gameapi.replay.session.Replay;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplayContainer implements eu.mcone.gameapi.api.replay.container.ReplayContainer {

    @Getter
    private final Replay replay;

    @Getter
    private final UUID containerUUID;
    @Getter
    private boolean playing = false;
    @Getter
    private AtomicInteger currentTick;
    @Getter
    private boolean forward = true;
    @Getter
    private boolean backward = false;
    @Setter
    private boolean showProgress = true;
    @Getter
    private ReplaySpeed replaySpeed = null;
    private int skipped;

    @Getter
    private final HashSet<Player> watchers;
    private final ServerRunner serverRunner;
    private final HashMap<UUID, eu.mcone.gameapi.replay.runner.PlayerRunner> running;

    private BukkitTask progressTask;

    @Getter
    private NPCInteractListener npcListener;

    @Getter
    private final ChunkHandler chunkHandler;

    @Getter
    private final HashMap<Integer, Integer> entities;

    public ReplayContainer(final Replay replay) {
        this.replay = replay;
        this.containerUUID = UUID.randomUUID();
        currentTick = new AtomicInteger();
        chunkHandler = new ChunkHandler(replay);
        watchers = new HashSet<>();
        serverRunner = new ServerRunner(this);
        running = new HashMap<>();
        entities = new HashMap<>();
    }

    public void addWatcher(final Player... players) {
        //Check if the loading is to slow
        chunkHandler.preLoad();

        running.forEach((key, value) -> value.addWatcher(players));

        for (Player player : players) {
            Bukkit.getPluginManager().callEvent(new ReplayWatcherJoinEvent(player, this));

            watchers.add(player);
            serverRunner.addWatcher(player);

            for (Player all : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(all);
                all.hidePlayer(player);
            }

            for (Player watcher : watchers) {
                player.showPlayer(watcher);
                watcher.showPlayer(player);
            }

            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.addWatcher(player);
                runner.getPlayer().getNpc().toggleVisibility(player, false);
            }
        }
    }

    private void removeAllWatchers() {
        for (Player player : watchers) {
            removeWatcher(player);
        }
    }

    public void removeWatcher(final Player... players) {
        running.forEach((key, value) -> value.removeWatcher(players));

        for (Player player : players) {
            watchers.remove(player);
            serverRunner.removeWatcher(player);

            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.removeWatcher(player);
                runner.getPlayer().getNpc().toggleVisibility(player, true);
            }

            for (Player watcher : watchers) {
                watcher.hidePlayer(player);
                player.hidePlayer(watcher);
            }

            for (Player all : Bukkit.getOnlinePlayers()) {
                if (!watchers.contains(all)) {
                    player.showPlayer(all);
                    all.showPlayer(all);
                }
            }

            Bukkit.getPluginManager().callEvent(new ReplayWatcherQuitEvent(player, this));
            if (playing && watchers.isEmpty()) {
                stop();
                replay.removeContainer(containerUUID);
            }
        }
    }

    private void progress() {
        progressTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getInstance(), () -> {
            for (Player watcher : watchers) {
                if (playing && showProgress) {
                    if (currentTick.get() == replay.getLastTick()) {
                        stop();
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
        if (!playing && running.size() == 0 && watchers.size() > 0) {
            playing = true;

            for (ReplayPlayer player : replay.getPlayersAsObject()) {
                if (!(running.containsKey(player.getUuid()))) {
                    player.setNpc(NpcUtils.constructNpcForPlayer(player, containerUUID));
                    eu.mcone.gameapi.replay.runner.PlayerRunner runner = new eu.mcone.gameapi.replay.runner.PlayerRunner(player, this);
                    runner.addWatcher(watchers.toArray(new Player[0]));
                    runner.play();
                    running.put(player.getUuid(), runner);
                }
            }

            serverRunner.play();
            progress();
            npcListener = new NPCInteractListener(this);
            Bukkit.getPluginManager().callEvent(new ReplayStartEvent(this, replay));
        }
    }

    public void stop() {
        if (playing && running.size() > 0) {
            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
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
            running.clear();
            Bukkit.getScheduler().cancelTask(progressTask.getTaskId());
            Bukkit.getPluginManager().callEvent(new ReplayStopEvent(getContainerUUID(), currentTick.get()));
            removeAllWatchers();
            currentTick.set(0);
        }
    }

    public void startPlaying() {
        if (!playing) {
            playing = true;
            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.startPlaying();
            }

            serverRunner.stopPlaying();

            Bukkit.getPluginManager().callEvent(new ReplayStartPlayingEvent(this, currentTick.get()));
        }
    }

    public void stopPlaying() {
        if (playing) {
            playing = false;
            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.stopPlaying();
            }

            serverRunner.stopPlaying();

            Bukkit.getPluginManager().callEvent(new ReplayStopPlayingEvent(this, currentTick.get()));
        }
    }

    public void forward() {
        if (playing && backward) {
            forward = true;
            backward = false;

            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.forward();
            }

            serverRunner.forward();
        }
    }

    public void backward() {
        if (playing && forward) {
            backward = true;
            forward = false;

            for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                runner.backward();
            }

            serverRunner.backward();
        }
    }

    public void setSpeed(ReplaySpeed speed) {
        if (playing) {
            ReplayChangeSpeedEvent changeSpeedEvent = new ReplayChangeSpeedEvent(replay.getID(), this, replaySpeed, speed);
            Bukkit.getPluginManager().callEvent(changeSpeedEvent);

            if (!changeSpeedEvent.isCancelled()) {
                this.replaySpeed = speed;

                for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                    runner.setReplaySpeed(speed);
                }

                serverRunner.setReplaySpeed(speed);
            }
        }
    }

    public void skip(int ticks) {
        if (playing) {
            ReplaySkipEvent skipEvent = new ReplaySkipEvent(replay.getID(), containerUUID);
            Bukkit.getPluginManager().callEvent(skipEvent);

            if (!skipEvent.isCancelled()) {
                for (eu.mcone.gameapi.replay.runner.PlayerRunner runner : running.values()) {
                    runner.skip(ticks);
                }

                currentTick.set(Math.max(currentTick.get() + ticks, 0));
                serverRunner.skip(ticks);
            }
        }
    }

    public void invite(final Player sender, final Player target) {
        CorePlayer corePlayer = CoreSystem.getInstance().getCorePlayer(sender);
        CorePlayer targetCorePlayer = CoreSystem.getInstance().getCorePlayer(target);

        if (corePlayer != null && targetCorePlayer != null) {
            if (watchers.contains(sender) && !watchers.contains(target)) {
                ReplayInvitePlayerEvent invitePlayerEvent = new ReplayInvitePlayerEvent(corePlayer, targetCorePlayer, replay.getID(), this);
                Bukkit.getPluginManager().callEvent(invitePlayerEvent);

                if (!invitePlayerEvent.isCancelled()) {
                    TextComponent tp = new TextComponent("§7Der Spieler §e" + sender.getName() + " §7lädt dich in ein Replay (" + replay.getID() + ") §7ein.");
                    tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7§lReplay beitreten \n§7§oLinksklick zum beitreten").create()));
                    tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/replay join " + sender.getName()));

                    GamePlugin.getGamePlugin().getMessenger().send(target, tp);
                    GamePlugin.getGamePlugin().getMessenger().send(sender, "§aDu hast den Spieler §e" + target.getName() + " §aerfolgreich eingeladen.");
                }
            } else {
                GamePlugin.getGamePlugin().getMessenger().send(sender, "§cDer Spieler ist bereits in deiner Session.");
            }
        }
    }

    public eu.mcone.gameapi.api.replay.runner.PlayerRunner createSingleRunner(final ReplayPlayer replayPlayer) {
        if (!running.containsKey(replayPlayer.getUuid())) {
            return new eu.mcone.gameapi.replay.runner.PlayerRunner(replayPlayer, this);
        } else {
            return null;
        }
    }

    public void openSpectatorInventory(Player player) {
        if (watchers.contains(player))
            new ReplaySpectatorInventory(replay, player);
    }
}
