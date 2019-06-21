/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.event;

import eu.mcone.gamesystem.api.game.achivements.SolvedAchievement;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class GameAchievementEvent extends Event {

    public static final HandlerList handlerList = new HandlerList();

    @Getter
    private final UUID uuid;
    @Getter
    private final SolvedAchievement solvedAchievement;

    public GameAchievementEvent(final UUID uuid, final SolvedAchievement solvedAchievement) {
        this.uuid = uuid;
        this.solvedAchievement = solvedAchievement;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
