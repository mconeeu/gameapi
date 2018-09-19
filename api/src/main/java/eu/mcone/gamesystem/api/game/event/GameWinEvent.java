package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameWinEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final Team team;

    public GameWinEvent(Team team) {
        this.team = team;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
