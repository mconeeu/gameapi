package eu.mcone.gameapi.api.replay.event.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReplayChangeSpeedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final String sessionID;
    private final ReplayContainer container;
    private final ReplaySpeed currentSpeed;
    private final ReplaySpeed newSpeed;
    @Setter
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
