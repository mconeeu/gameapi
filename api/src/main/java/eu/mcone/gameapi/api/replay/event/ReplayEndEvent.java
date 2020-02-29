package eu.mcone.gameapi.api.replay.event;

import eu.mcone.gameapi.api.replay.session.ReplaySession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class ReplayEndEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final ReplaySession session;
    private final int lastTick;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
