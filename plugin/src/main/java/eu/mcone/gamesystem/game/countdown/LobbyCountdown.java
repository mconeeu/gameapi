package eu.mcone.gamesystem.game.countdown;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.event.GameCountdownEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LobbyCountdown implements GameCountdown {

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
    private boolean isWaiting, isRunning;

    public LobbyCountdown(int seconds) {
        try {
            if (GameTemplate.getInstance() != null) {
                if (seconds >= 60) {
                    this.seconds = seconds;
                    this.staticSeconds = seconds;
                    this.isRunning = true;
                } else {
                    throw new GameSystemException("The specified seconds must be bigger than 60 seconds");
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
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
        if (!(Bukkit.getScheduler().isCurrentlyRunning(idleTaskID))) {
            if (Bukkit.getScheduler().isCurrentlyRunning(runTaskID)) {
                Bukkit.getScheduler().cancelTask(runTaskID);
            }

            this.isRunning = false;
            this.isWaiting = true;

            reset();

            this.idleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameTemplate.getInstance(), () -> {
                int missing = Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size();
                for (Player playing : GameTemplate.getInstance().getPlaying()) {
                    GameTemplate.getInstance().getMessager()
                            .send(playing, CoreSystem.getInstance().getTranslationManager().get("game.countdown.lobby.missing", CoreSystem.getInstance().getGlobalCorePlayer(playing.getUniqueId()))
                                    .replace("%missing%", Integer.toString(missing)));
                }
            }, 0L, 500L);
        }
    }

    public void reset() {
        this.seconds = staticSeconds;

        for (Player all : GameTemplate.getInstance().getPlaying()) {
            all.setLevel(staticSeconds);
        }
    }

    public void stop() {
        reset();
        this.isWaiting = false;
        this.isRunning = false;
        if (Bukkit.getScheduler().isCurrentlyRunning(runTaskID)) Bukkit.getScheduler().cancelTask(runTaskID);
        if (Bukkit.getScheduler().isCurrentlyRunning(idleTaskID)) Bukkit.getScheduler().cancelTask(idleTaskID);
    }
}
