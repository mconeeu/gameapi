package eu.mcone.gameapi.api.event.team;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamDestroyEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final Team team;

    public TeamDestroyEvent(final Team team) {
        this.team = team;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
