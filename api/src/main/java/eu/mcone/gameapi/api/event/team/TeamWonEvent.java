package eu.mcone.gameapi.api.event.team;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TeamWonEvent extends Event implements Cancellable {

    private static final HandlerList handlerlist = new HandlerList();

    private final Team team;
    @Setter
    private boolean cancelled;

    public TeamWonEvent(final Team team) {
        this.team = team;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
