/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.manager.map.GameMapItem;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameMapChangeEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    @Getter
    private final GameMapItem mapItem;

    public GameMapChangeEvent(final GameMapItem mapItem) {
        this.mapItem = mapItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

