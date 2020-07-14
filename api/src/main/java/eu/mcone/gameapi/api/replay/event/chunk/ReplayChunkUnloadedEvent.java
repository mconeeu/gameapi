package eu.mcone.gameapi.api.replay.event.chunk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class ReplayChunkUnloadedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final String sessionID;
    private final String chunkID;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
