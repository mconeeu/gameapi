package eu.mcone.gameapi.api.replay.event.container;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReplayContainerCreatedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final UUID containerID;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
