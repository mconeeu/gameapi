/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.gamestate.GameStateID;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameStateChangeEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final GameStateID gameStateID;

    public GameStateChangeEvent(final GameStateID gameStateID) {
        this.gameStateID = gameStateID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
