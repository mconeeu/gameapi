/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.achievement.GameAchievementManager;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AchievementInventory extends CoreInventory {

    public AchievementInventory(Player player, GameAchievementManager achievementManager) {
        this(player, achievementManager, achievementManager.getPlugin().getGamemode());
    }

    public AchievementInventory(Player player, GameAchievementManager achievementManager, Gamemode gamemode) {
        super("§8» §c§lAchievement", player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);
        GameAPIPlayer gamePlayer = GameAPIPlugin.getSystem().getGamePlayer(player.getUniqueId());

        if (!achievementManager.getAchievements(gamemode).isEmpty()) {
            int i = 0;
            for (Achievement achievement : achievementManager.getAchievements(gamemode)) {
                if (gamePlayer.hasAchievement(achievement.getName())) {
                    setItem(i, new ItemBuilder(Material.INK_SACK, 1, 10).displayName("§7§o" + achievement.getName()).lore("§7§o" + achievement.getDescription()).create());

                } else {
                    setItem(i, new ItemBuilder(Material.INK_SACK, 0, 1).displayName("§7§o" + achievement.getName()).lore("§7§o???").create());

                }

                i++;
            }
        } else {
            setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.BARRIER, 1, 0).displayName("§cKeine Achievements vorhanden!").create());
        }

        openInventory();
    }

}
