package eu.mcone.gameapi.api.event.backpack;

import eu.mcone.gameapi.api.backpack.BackpackManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public final class BackpackManagerStopEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final BackpackManager manager;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
