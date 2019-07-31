package eu.mcone.gamesystem.api.game.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameMapCountdownChangeEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    @Getter
    private final int seconds, runningTaskID;
    @Getter
    private final boolean isRunning;

    public GameMapCountdownChangeEvent(final int seconds, final boolean isRunning, final int runningTaskID) {
        this.seconds = seconds;
        this.isRunning = isRunning;
        this.runningTaskID = runningTaskID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
