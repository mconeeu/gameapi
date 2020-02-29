package eu.mcone.gameapi.api.event.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class TeamWonEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final Team team;
    @Getter
    private final List<GamePlayer> winners;

    public TeamWonEvent(final Team team, final List<GamePlayer> winners) {
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
