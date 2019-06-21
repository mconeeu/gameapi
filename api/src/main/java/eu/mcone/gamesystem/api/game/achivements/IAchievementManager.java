/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.achivements;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface IAchievementManager {

    Gamemode getGamemode();

    void loadAchievements();

    void loadSolvedAchievement(UUID uuid);

    void loadSolvedAchievements();

    void registerAchievement(final Achievement achievement);

    void registerAchievements(final Achievement... achievements);

    SolvedAchievement solveAchievement(final UUID uuid, final String achievementName);

    void solveAchievements(final UUID uuid, final String... solvedAchievementName);

    void removeSolvedAchievement(final UUID uuid, final String achievementName);

    void removeSolvedAchievements(final UUID uuid, final String... solvedAchievementNames);

    boolean hasAchievement(final UUID uuid, final String achievementName);

    Achievement getAchievement(final String achievementName);

    ArrayList<SolvedAchievement> getSolvedAchievements(final UUID uuid);

    ArrayList<Achievement> getAchievements();

    void openAchievementInventory(final Player player);
}
