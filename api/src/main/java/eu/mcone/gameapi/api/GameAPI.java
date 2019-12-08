/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.map.MapManager;
import lombok.Getter;
import org.bukkit.ChatColor;

public abstract class GameAPI extends CorePlugin {

    public GameAPI() {
        super("gamesystem", ChatColor.DARK_GRAY, "system.prefix.server");
    }

    @Getter
    private static GameAPI instance;

    protected void setInstance(final GameAPI instance) {
        if (instance == null) {
            System.err.println("GameSystem instance cannot be set twice!");
        } else {
            GameAPI.instance = instance;
        }
    }

    public abstract MapManager constructMapManager();

    public abstract BackpackManager constructBackpackManager(GamePlugin<?> gamePlugin, Option... options);

    public abstract AchievementManager constructAchievementManager(GamePlugin<?> gamePlugin, Option... options);
}
