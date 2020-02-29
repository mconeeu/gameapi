package eu.mcone.gameapi.api.event.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class PlayerRoundStatsChangeEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final Player player;
    private final int kills;
    private final int deaths;
    private final int goals;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
