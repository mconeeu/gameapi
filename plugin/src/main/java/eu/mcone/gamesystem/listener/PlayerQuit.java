package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (GameTemplate.getInstance().getPlaying().contains(p)) {
            GameTemplate.getInstance().getGamePlayer(p.getUniqueId()).destroy();

            if (GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)) {
                GameCountdown gameCountdown = GameTemplate.getInstance().getGameCountdownHandler().getGameCountdown(GameCountdownID.LOBBY_COUNTDOWN);
                if (Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size() > 0) {
                    if (!(Bukkit.getScheduler().isCurrentlyRunning(gameCountdown.getIdleTaskID()))) gameCountdown.idle();
                } else if (Bukkit.getScheduler().isCurrentlyRunning(gameCountdown.getIdleTaskID())) {
                    Bukkit.getScheduler().cancelTask(gameCountdown.getIdleTaskID());
                }
            }
        }
    }
}
