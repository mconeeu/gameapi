package eu.mcone.gameapi.api.gamestate;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateTimeoutEvent;
import lombok.Getter;

@Getter
public class GameState {

    private final String name;
    private final long timeout;
    private final int countdown;

    public GameState(String name, long nextStateSeconds, int countdownSeconds) {
        this.name = name;
        this.timeout = nextStateSeconds;
        this.countdown = countdownSeconds;
    }

    public GameState(String name, long nextStateSeconds) {
        this(name, nextStateSeconds, 0);
    }

    public GameState(String name) {
        this(name, 0);
    }

    public void onCountdownStart(GameStateCountdownEvent event) {}

    public void onStart(GameStateStartEvent event) {}

    public void onTimeoutEnd(GameStateTimeoutEvent event) {}

    public void onEnd(GameStateEndEvent event) {}

    public void onCountdownSecond(CorePlugin plugin, int second) {}

    public boolean hasCountdown() {
        return countdown > 0;
    }

    public boolean hasTimeout() {
        return timeout > 0;
    }

}
