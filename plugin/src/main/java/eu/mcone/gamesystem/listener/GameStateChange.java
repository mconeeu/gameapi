package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.event.GameStateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStateChange implements Listener {

    @EventHandler
    public void on(GameStateChangeEvent e) {
        if (GameTemplate.getInstance().getGameStateHandler().hasGameCountdown(e.getGameStateID())) {
            GameTemplate.getInstance().getGameStateHandler().getGameCountdown(e.getGameStateID()).run();
        }
    }
}
