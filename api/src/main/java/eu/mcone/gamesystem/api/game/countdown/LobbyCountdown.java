/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.countdown;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.event.GameCountdownEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyCountdown implements IGameCountdown {

    private Logger log;
    @Getter
    private final GameCountdownID ID = GameCountdownID.LOBBY_COUNTDOWN;
    @Setter
    @Getter
    private int seconds;
    @Getter
    private int staticSeconds;
    @Getter
    private int runTaskID, idleTaskID;
    @Getter
    private boolean isIdling, isRunning;

    public LobbyCountdown(int seconds) {
        log = GameSystemAPI.getInstance().getLogger();

        try {
            if (GameTemplate.getInstance() != null) {
                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_GAME_STATE_HANDLER)
                        || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                    if (seconds >= 60) {
                        this.seconds = seconds;
                        this.staticSeconds = seconds;
                        this.isRunning = true;
                    } else {
                        throw new GameSystemException("The specified seconds must be bigger than 60 seconds");
                    }
                } else {
                    throw new GameSystemException("The option 'USE_GAME_STATE_HANDLER' was not activated");
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Exception in LobbyCountdown", e);
        }
    }

    public void run() {
        if (!Bukkit.getScheduler().isCurrentlyRunning(runTaskID)) {
            isRunning = true;
            isIdling = false;

            this.runTaskID = Bukkit.getScheduler().runTaskTimer(GameTemplate.getInstance(), () -> {
                Bukkit.getServer().getPluginManager().callEvent(
                        new GameCountdownEvent
                                (
                                        ID,
                                        seconds,
                                        isRunning,
                                        runTaskID
                                )
                );

                if (this.seconds != 0) {
                    for (Player a : GameTemplate.getInstance().getPlaying()) a.setLevel(this.seconds);
                }

                switch (this.seconds) {
                    case 60:
                    case 30:
                    case 15:
                    case 10:
                    case 5:
                    case 3:
                    case 2:
                    case 1:
                        for (Player playing : GameTemplate.getInstance().getPlaying()) {
                            GameTemplate.getInstance().getMessager()
                                    .send(playing, CoreSystem.getInstance().getTranslationManager().get("game.countdown.lobby.idling", CoreSystem.getInstance().getGlobalCorePlayer(playing.getUniqueId()))
                                            .replace("%seconds%", Integer.toString(this.seconds)));
                            playing.playSound(playing.getLocation(), Sound.LEVEL_UP, 1, 1);
                        }
                        break;

                    case 0:
                        if (GameTemplate.getInstance().getPlaying().size() < Playing.Min_Players.getValue()) {
                            for (Player playing : GameTemplate.getInstance().getPlaying())
                                GameTemplate.getInstance().getMessager().sendTransl(playing, "game.lobby.toFewPlayers");
                            this.stop();
                            this.idle();
                            return;
                        } else {
                            this.forceStop();
                        }

                        break;
                    default:
                        break;
                }
                this.seconds--;
            }, 0L, 20L).getTaskId();
        }
    }

    public void idle() {
        if (!Bukkit.getScheduler().isCurrentlyRunning(idleTaskID)) {
            stopRunning();

            this.idleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameTemplate.getInstance(), () -> {
                int missing = Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size();
                if (missing > 0) {
                    for (Player playing : GameTemplate.getInstance().getPlaying()) {
                        GameTemplate.getInstance().getMessager()
                                .send(playing, CoreSystem.getInstance().getTranslationManager().get("game.countdown.lobby.missing", CoreSystem.getInstance().getGlobalCorePlayer(playing.getUniqueId()))
                                        .replace("%missing%", Integer.toString(missing)));
                    }
                } else {
                    Bukkit.getScheduler().cancelTask(idleTaskID);
                }
            }, 0L, 600L);
        }
    }

    public void reset() {
        log.info("Reset LobbyCountdown");

        this.seconds = staticSeconds;
        for (Player all : GameTemplate.getInstance().getPlaying()) {
            all.setLevel(staticSeconds);
        }
    }

    public void stop() {
        log.info("Stop LobbyCountdown");

        reset();

        this.isIdling = false;
        this.isRunning = false;

        Bukkit.getScheduler().cancelTask(runTaskID);
        Bukkit.getScheduler().cancelTask(idleTaskID);
    }

    @Override
    public void stopRunning() {
        reset();

        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(runTaskID);

    }

    @Override
    public void stopIdling() {
        this.isIdling = false;
        Bukkit.getScheduler().cancelTask(idleTaskID);
    }

    public void forceStop() {
        log.log(Level.WARNING, "Forcestop LobbyCountdown");

        Bukkit.getScheduler().cancelTask(runTaskID);
        log.log(Level.WARNING, "Cancel running Task");

        Bukkit.getScheduler().cancelTask(idleTaskID);
        log.log(Level.WARNING, "Cancel idle Task");
    }
}
