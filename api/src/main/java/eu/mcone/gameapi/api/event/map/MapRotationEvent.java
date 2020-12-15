/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.event.map;

import eu.mcone.gameapi.api.map.GameAPIMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class MapRotationEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final GameAPIMap fromMap, toMap;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
