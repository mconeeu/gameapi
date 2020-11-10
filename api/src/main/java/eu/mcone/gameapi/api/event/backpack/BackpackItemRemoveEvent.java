package eu.mcone.gameapi.api.event.backpack;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class BackpackItemRemoveEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer player;
    private final Category category;
    private final BackpackItem item;
    @Setter
    private boolean applyRankBoots;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
