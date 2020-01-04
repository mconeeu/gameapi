package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStateStopEvent extends GameStateEvent {

    public GameStateStopEvent(GameStateManager gameStateManager, GameState current, GameState next) {
        super(gameStateManager);
        this.current = current;
        this.next = next;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameState current, next;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
