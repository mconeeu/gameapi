package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStateStartEvent extends GameStateEvent {

    public GameStateStartEvent(GameStateManager gameStateManager, GameState previous, GameState current) {
        super(gameStateManager);
        this.timeout = current.getTimeout();
        this.previous = previous;
        this.current = current;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameState previous, current;
    @Setter
    private long timeout;
    @Setter
    private boolean skipped;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
