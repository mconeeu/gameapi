package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class GamePlayerLoadedEvent extends Event {
    @Getter
    private final static HandlerList handlerList = new HandlerList();

    private GamePlayer player;
    private GamePlayerLoadedEvent.Reason reason;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum Reason {
        JOINED, RELOADED
    }
}