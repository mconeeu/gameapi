package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class GameStateEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameStateManager gameStateManager;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
