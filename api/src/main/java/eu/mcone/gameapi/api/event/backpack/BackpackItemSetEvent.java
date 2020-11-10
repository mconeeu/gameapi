package eu.mcone.gameapi.api.event.backpack;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class BackpackItemSetEvent extends Event implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer player;
    private final Category category;
    private final BackpackItem item;
    @Setter
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
