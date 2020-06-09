package eu.mcone.gameapi.api.event.onepass;

import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class LevelChangeEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer player;
    private final int fromLevel;

    public HandlerList getHandlers() {
        return handlerList;
    }
}
