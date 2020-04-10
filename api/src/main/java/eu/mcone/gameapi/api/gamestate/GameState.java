package eu.mcone.gameapi.api.gamestate;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.api.event.gamestate.*;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Getter
public class GameState implements Listener {

    private final String name;
    private final int countdown;
    private final long timeout;

    public GameState(String name) {
        this(name, 0);
    }

    public GameState(String name, int countdown) {
        this(name, countdown, 0);
    }

    public GameState(String name, int countdown, long timeout) {
        this.name = name;
        this.countdown = countdown;
        this.timeout = timeout;
    }

    @EventHandler
    public void onStart(GameStateStartEvent event) {}

    public void onTimeoutStart(GameStateTimeoutStartEvent event) {}

    public void onTimeoutEnd(GameStateTimeoutEndEvent event) {}

    public void onCountdownStart(GameStateCountdownStartEvent event) {}

    public void onCountdownEnd(GameStateCountdownEndEvent event) {}

    public void onStop(GameStateStopEvent event) {}

    public void onTimeoutSecond(CorePlugin plugin, long second) {}

    public void onCountdownSecond(CorePlugin plugin, int second) {}

    public boolean hasCountdown() {
        return countdown > 0;
    }

    public boolean hasTimeout() {
        return timeout > 0;
    }

}
