package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStateTimeoutEvent extends GameStateEvent implements Cancellable {

    public GameStateTimeoutEvent(GameStateManager gameStateManager, GameState current, GameState next) {
        super(gameStateManager);
        this.current = current;
        this.next = next;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameState current, next;

    @Setter
    private boolean cancelled;
    @Setter
    private String cancelReason;
    @Setter
    private long newTimeout;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
