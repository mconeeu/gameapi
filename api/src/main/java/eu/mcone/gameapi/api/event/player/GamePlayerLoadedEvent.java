/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.event.player;

import eu.mcone.coresystem.api.bukkit.event.player.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class GamePlayerLoadedEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final CorePlayerLoadedEvent corePlayerLoadedEvent;
    private final Player bukkitPlayer;
    private final CorePlayer corePlayer;
    private final GamePlayer player;

    public HandlerList getHandlers() {
        return handlerList;
    }

}
