package eu.mcone.gamesystem.game.countdown;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.event.GameCountdownEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpawnCountdown implements GameCountdown {

    @Getter
    private final GameCountdownID ID = GameCountdownID.SPAWN_COUNTDOWN;
    @Setter
    @Getter
    private int seconds;
    @Getter
    private int staticSeconds;
    @Getter
    private int runTaskID, idleTaskID;
    @Getter
    private boolean isRunning;

    public SpawnCountdown(int seconds) {
        try {
            if (GameTemplate.getInstance() != null) {
                if (seconds >= 5) {
                    this.seconds = seconds;
                    this.staticSeconds = seconds;
                    this.isRunning = true;
                } else {
                    throw new GameSystemException("The specified seconds must be bigger than 5 seconds");
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
                    case 30:
                    case 15:
                    case 10:
                    case 5:
                    case 3:
                    case 2:
                    case 1:
                        for (Player p : GameTemplate.getInstance().getPlaying()) {
                            GameTemplate.getInstance().getMessager().sendTransl(p, "game.countdown.spawn".replace("%seconds%", Integer.toString(this.seconds)));
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
        System.out.println("No idling defined");
    }

    public void reset() {
        for (Player all : GameTemplate.getInstance().getPlaying()) {
            all.setLevel(staticSeconds);
        }
    }

    public void stop() {
        for (Player all : GameTemplate.getInstance().getPlaying()) {
            all.setLevel(staticSeconds);
        }

        this.seconds = staticSeconds;
        this.isRunning = false;
        Bukkit.getScheduler().cancelTask(this.runTaskID);
    }
}
