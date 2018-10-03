package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.gamestate.GameStateID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameStateChangeEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();
    private final GameStateID gameStateID;

    public GameStateChangeEvent(GameStateID gameStateID) {
        this.gameStateID = gameStateID;
    }

    public GameStateID getGameStateID() {
        return gameStateID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
