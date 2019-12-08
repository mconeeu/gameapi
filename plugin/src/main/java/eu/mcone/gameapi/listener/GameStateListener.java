package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateTimeoutEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStateListener implements Listener {

    @EventHandler
    public void onGameStateCountdown(GameStateCountdownEvent e) {
        e.getCurrent().onCountdownStart(e);
    }

    @EventHandler
    public void onGameStateStart(GameStateStartEvent e) {
        e.getCurrent().onStart(e);
    }

    @EventHandler
    public void onGameStateTimeout(GameStateTimeoutEvent e) {
        e.getCurrent().onTimeoutEnd(e);
    }

    @EventHandler
    public void onGameStateEnd(GameStateEndEvent e) {
        e.getCurrent().onEnd(e);
    }

}
