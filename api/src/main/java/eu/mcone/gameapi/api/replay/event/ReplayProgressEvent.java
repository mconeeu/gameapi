package eu.mcone.gameapi.api.replay.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class ReplayProgressEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

}