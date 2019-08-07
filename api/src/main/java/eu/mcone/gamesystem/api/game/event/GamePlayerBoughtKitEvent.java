package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class GamePlayerBoughtKitEvent extends Event {
    @Getter
    private final static HandlerList handlerList = new HandlerList();

    private GamePlayer player;
    private Kit kit;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}