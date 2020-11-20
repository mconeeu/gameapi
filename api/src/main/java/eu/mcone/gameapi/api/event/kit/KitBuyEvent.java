package eu.mcone.gameapi.api.event.kit;

import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public final class KitBuyEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer player;
    private final Kit kit;
    private final boolean autoBuy;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
