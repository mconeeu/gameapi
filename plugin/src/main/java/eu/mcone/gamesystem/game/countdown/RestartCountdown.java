package eu.mcone.gamesystem.game.countdown;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.event.GameCountdownEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RestartCountdown implements GameCountdown {

    private Logger log;

    @Getter
    private final GameCountdownID ID = GameCountdownID.RESTART_COUNTDOWN;
    @Setter
    @Getter
    private int seconds;
    @Getter
    private int staticSeconds;
    @Getter
    private int runTaskID, idleTaskID;
    @Getter
    private boolean isRunning;

    public RestartCountdown(int seconds) {
        log = GameSystemAPI.getInstance().getLogger();

        try {
            if (GameTemplate.getInstance() != null) {
                if (seconds >= 30) {
                    this.seconds = seconds;
                    this.staticSeconds = seconds;
                    this.isRunning = true;
                } else {
                    throw new GameSystemException("The specified seconds must be bigger than 30 seconds");
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Exception in RestartCountdown", e);
        }
    }

    public void run() {
        if (!(Bukkit.getScheduler().isCurrentlyRunning(runTaskID))) {
            this.runTaskID = Bukkit.getScheduler().runTaskTimerAsynchronously(GameTemplate.getInstance(), () -> {
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
                    case 30:
                    case 15:
                    case 10:
                    case 5:
                    case 3:
                    case 2:
                    case 1:
                        for (Player playing : GameTemplate.getInstance().getPlaying()) {
                            GameTemplate.getInstance().getMessager()
                                    .send(playing, CoreSystem.getInstance().getTranslationManager().get("game.countdown.restart", CoreSystem.getInstance().getGlobalCorePlayer(playing.getUniqueId()))
                                            .replace("%seconds%", Integer.toString(this.seconds)));
                        }
                        break;

                    case 0:
                        Bukkit.getServer().shutdown();
                        break;
                    default:
                        break;
                }
                this.seconds--;
            }, 0L, 20L).getTaskId();
        }
    }

    @Override
    public void idle() {
        log.info("No idling task defined for this countdown");
    }

    public void reset() {
        log.info("Reset RestartCountdown");
        for (Player all : GameTemplate.getInstance().getPlaying()) {
            all.setLevel(staticSeconds);
        }
    }

    public void stop() {
        log.info("Stop RestartCountdown");
        reset();

        this.seconds = staticSeconds;
        this.isRunning = false;

        Bukkit.getScheduler().cancelTask(runTaskID);
    }

    public void forceStop() {
        log.log(Level.WARNING, "Forcestop RestartCountdown");

        Bukkit.getScheduler().cancelTask(runTaskID);
        log.log(Level.WARNING, "Cancel running Task");
    }
}
