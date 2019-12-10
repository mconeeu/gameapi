/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BackpackInventory extends CategoryInventory {

    @Setter
    protected static GamePlugin<?> plugin;

    protected final GameAPIPlayer<?> gamePlayer;

    public BackpackInventory(Player p, Category category) {
        super("§8» §3§lRucksack", p, category.getItemStack());
        this.gamePlayer = plugin.getGamePlayer(p.getUniqueId());

        Map<Integer, Category> sortedCategories = new HashMap<>();
        for (Category cat : ((GameBackpackManager) plugin.getBackpackManager()).getCategories()) {
            sortedCategories.put(cat.getSort(), cat);
        }

        for (Category cat : new TreeMap<>(sortedCategories).values()) {
            if (cat.isShowInBackpack()) {
                addCategory(cat.getItemStack());
            }
        }

        ((GameBackpackManager) plugin.getBackpackManager()).onCategoryInventoryCreate(category, gamePlayer, this, p);
        openInventory();
    }

    @Override
    protected void openCategoryInventory(ItemStack itemStack, Player player) {
        new BackpackInventory(player, ((GameBackpackManager) plugin.getBackpackManager()).getCategoryByItemDisplayName(itemStack.getItemMeta().getDisplayName())).openInventory();
    }

}
