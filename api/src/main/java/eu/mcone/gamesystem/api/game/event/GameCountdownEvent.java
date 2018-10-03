package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameCountdownEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();
    private GameCountdownID ID;
    private int seconds;
    private boolean isRunning;
    private int runningTaskID;

    public GameCountdownEvent(GameCountdownID ID, int seconds, boolean isRunning, int runningTaskID) {
        this.ID = ID;
        this.seconds = seconds;
        this.isRunning = isRunning;
        this.runningTaskID = runningTaskID;
    }

    public GameCountdownID getID() {
        return ID;
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTaskID() {
        return runningTaskID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
