package eu.mcone.gameapi.api.replay.event.runner;

import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.session.Replay;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ReplayWatcherQuitEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final ReplayContainer container;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
