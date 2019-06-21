/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public final class GameWinEvent extends Event {

    private static final HandlerList handlerlist = new HandlerList();

    @Getter
    private final Team team;
    @Getter
    private final List<IGamePlayer> winners;

    public GameWinEvent(final Team team, final List<IGamePlayer> winners) {
        this.team = team;
        this.winners = winners;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public static HandlerList getHandlerList() {
        return handlerlist;
    }
}
