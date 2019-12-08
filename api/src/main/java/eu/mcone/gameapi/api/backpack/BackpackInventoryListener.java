package eu.mcone.gameapi.api.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class BackpackInventoryListener {

    @Setter
    protected static GamePlugin<?> plugin;

    public void onBackpackInventoryClick(BackpackItem item, GameAPIPlayer<?> gamePlayer, Player player) {}

    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GameAPIPlayer<?> gamePlayer, Player player) {
        for (BackpackItem item : categoryItems) {
            if (gamePlayer.hasBackpackItem(category.getName(), item) || (!category.getName().equals("STORY_ITEMS") && player.hasPermission("system.game.items"))) {
                inv.addItem(item.getItem(), e -> onBackpackInventoryClick(item, gamePlayer, player));
            }
        }
    }

}
