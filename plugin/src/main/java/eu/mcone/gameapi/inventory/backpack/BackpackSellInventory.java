/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class BackpackSellInventory extends CategoryInventory {

    @Setter
    protected static GamePlugin<?> plugin;

    protected final GameAPIPlayer<?> gamePlayer;
    private final Map<String, Set<BackpackItem>> ownItems;

    public BackpackSellInventory(Player p, Category category) {
        super("§8» §3§lRucksack", p, category.getItemStack());
        this.gamePlayer = plugin.getGamePlayer(p.getUniqueId());
        this.ownItems = gamePlayer.getBackpackItems();

        for (Category cat : ((GameBackpackManager) plugin.getBackpackManager()).getCategories()) {
            if (cat.isSellable()) {
                addCategory(cat.getItemStack());
            }
        }

        for (BackpackItem item : ((GameBackpackManager) plugin.getBackpackManager()).getCategoryItems(category.getName())) {
            if (hasItem(category.getName(), item) && item.isSellable()) {
                addItem(item.getItem(), e -> new VendorSellInventory(p, gamePlayer, category, item));
            }
        }

        openInventory();
    }

    private boolean hasItem(String category, BackpackItem item) {
        return ownItems.getOrDefault(category, Collections.emptySet()).contains(item);
    }

    @Override
    protected void openCategoryInventory(ItemStack itemStack, Player player) {
        new BackpackSellInventory(player, ((GameBackpackManager) plugin.getBackpackManager()).getCategoryByItemDisplayName(itemStack.getItemMeta().getDisplayName())).openInventory();
    }

}
