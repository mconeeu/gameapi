package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class GamePlayerLoaded extends Event {
    @Getter
    private final static HandlerList handlerList = new HandlerList();

    private GamePlayer player;
    private GamePlayerLoaded.Reason reason;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum Reason {
        JOINED, RELOADED
    }
}
