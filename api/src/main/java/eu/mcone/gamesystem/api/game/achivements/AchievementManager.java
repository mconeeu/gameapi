/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.achivements;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface AchievementManager {

    Gamemode getGamemode();

    void loadAchievements();

    void registerAchievement(final Achievement achievement);

    void registerAchievements(final Achievement... achievements);

    Achievement getAchievement(final String achievementName);

    ArrayList<Achievement> getAchievements();

    void openAchievementInventory(final Player player);
}
