/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.event.gamestate;

import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStateStartEvent extends GameStateEvent implements Cancellable {

    public GameStateStartEvent(GameStateManager gameStateManager, GameState previous, GameState current) {
        super(gameStateManager);
        this.previous = previous;
        this.current = current;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameState previous, current;
    @Setter
    private boolean cancelled;
    @Setter
    private boolean skipGameState;

    public HandlerList getHandlers() {
        return handlerList;
    }

}