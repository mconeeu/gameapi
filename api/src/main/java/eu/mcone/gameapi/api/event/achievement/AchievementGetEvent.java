package eu.mcone.gameapi.api.event.achievement;

import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class AchievementGetEvent extends Event implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GamePlayer player;
    private final Achievement achievement;
    @Setter
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
