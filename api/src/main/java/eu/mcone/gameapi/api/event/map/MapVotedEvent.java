package eu.mcone.gameapi.api.event.map;

import eu.mcone.gameapi.api.map.GameAPIMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class MapVotedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameAPIMap map;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
