package eu.mcone.gameapi.api.replay.event.runner;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReplayInvitePlayerEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final CorePlayer player;
    private final CorePlayer target;
    private final String sessionID;
    private final ReplayContainer container;
    @Setter
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
