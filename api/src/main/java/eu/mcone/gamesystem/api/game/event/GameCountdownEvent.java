/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameCountdownEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    @Getter
    private final GameCountdownID ID;
    @Getter
    private final int seconds, runningTaskID;
    @Getter
    private final boolean isRunning;

    public GameCountdownEvent(final GameCountdownID ID, final int seconds, final boolean isRunning, final int runningTaskID) {
        this.ID = ID;
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
