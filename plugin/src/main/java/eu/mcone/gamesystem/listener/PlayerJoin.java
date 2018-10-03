package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import eu.mcone.gamesystem.game.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    public PlayerJoin() {
        Bukkit.getServer().getPluginManager().registerEvents(this, GameSystem.getInstance());
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new GamePlayer(p);

        if (GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)) {
            GameCountdown gameCountdown = GameTemplate.getInstance().getGameCountdownHandler().getGameCountdown(GameCountdownID.LOBBY_COUNTDOWN);
            if (Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size() <= 0) {
                if (!(gameCountdown.isRunning())) {
                    if (Bukkit.getScheduler().isCurrentlyRunning(gameCountdown.getIdleTaskID())) {
                        Bukkit.getScheduler().cancelTask(gameCountdown.getIdleTaskID());
                    }

                    gameCountdown.run();
                }
            } else {
                if (!(Bukkit.getScheduler().isCurrentlyRunning(gameCountdown.getIdleTaskID()))) {
                    if (Bukkit.getScheduler().isCurrentlyRunning(gameCountdown.getRunTaskID())) {
                        Bukkit.getScheduler().cancelTask(gameCountdown.getRunTaskID());
                    }

                    gameCountdown.idle();
                }
            }
        }
    }
}
