package eu.mcone.gameapi.api.event.game;

import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public final class GameDrawEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final Team team;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
