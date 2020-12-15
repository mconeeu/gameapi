/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.achievement;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface AchievementManager {

    void registerAchievements(Achievement... achievements);

    void reload();

    Achievement getAchievement(String name);

    Achievement getAchievement(Gamemode gamemode, String name);

    List<Achievement> getAchievements(Gamemode gamemode);

    boolean setAchievement(GamePlayer player, Achievement achievement);

    void openAchievementInventory(Player p);

    void openAchievementInventory(Player p, Gamemode gamemode);

}
