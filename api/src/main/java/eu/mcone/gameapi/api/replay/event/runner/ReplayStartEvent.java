package eu.mcone.gameapi.api.replay.event.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.session.Replay;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class ReplayStartEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private final ReplayContainer container;
    private final Replay replay;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
