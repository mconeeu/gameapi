package eu.mcone.gameapi.api.event.team;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class TeamWonEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final Team team;
    @Getter
    private final Collection<Player> winners;

    public TeamWonEvent(final Team team, final Collection<Player> winners) {
        this.team = team;
        this.winners = winners;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
