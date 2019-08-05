/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.achivements;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.achivements.Achievement;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

class AchievementInventory extends CoreInventory {

    AchievementInventory(Player player) {
        super("§8» §c§lAchievement §8┋ §f§o" + player.getName(), player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);
        GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(player.getUniqueId());

        for (int i = 0; i < GameTemplate.getInstance().getAchievementManager().getAchievements().size(); i++) {
            Achievement achievement = GameTemplate.getInstance().getAchievementManager().getAchievements().get(i);
            if (gamePlayer.hasAchievement(achievement.getAchievementName())) {
                setItem(i, new ItemBuilder(Material.INK_SACK, 1, 10).displayName("§7§o" + achievement.getAchievementName()).lore("§7§o" + achievement.getDescription()).create());
            } else {
                setItem(i, new ItemBuilder(Material.INK_SACK, 0, 1).displayName("§7§o" + achievement.getAchievementName()).lore("§7§o" + achievement.getDescription()).create());
            }
        }
        openInventory();
    }
}
