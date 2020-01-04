package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStateCountdownEndEvent extends GameStateEvent {

    public GameStateCountdownEndEvent(GameStateManager gameStateManager, GameState gameState) {
        super(gameStateManager);
        this.gameState = gameState;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameState gameState;

    @Setter
    private boolean cancelled;
    @Setter
    private int newCountdown;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
