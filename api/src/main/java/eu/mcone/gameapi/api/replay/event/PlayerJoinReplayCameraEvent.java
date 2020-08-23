package eu.mcone.gameapi.api.replay.event;

import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerJoinReplayCameraEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final ReplayContainer container;
    private final Player player;
    private final PlayerNpc playerNpc;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
