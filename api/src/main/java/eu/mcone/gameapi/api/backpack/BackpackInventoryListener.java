package eu.mcone.gameapi.api.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class BackpackInventoryListener {

    @Setter
    protected static GamePlugin plugin;

    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player player) {
    }

    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gamePlayer, Player player) {
        for (BackpackItem item : categoryItems) {
            if (gamePlayer.hasBackpackItem(category.getName(), item) || (!category.getName().equals("STORY_ITEMS") && player.hasPermission("system.game.items"))) {
                if (!plugin.getBackpackManager().getDisabledItems().contains(item.getName())) {
                    inv.addItem(item.getItem(), e -> {
                        if (gamePlayer.isEffectsVisible()) {
                            onBackpackInventoryClick(item, gamePlayer, player);
                        } else {
                            player.closeInventory();
                            GameAPI.getInstance().getMessenger().send(player, "§4Du kannst keine Effekte benutzen, da Effekte von anderen für dich unsichtbar sind");
                        }
                    });
                }
            }
        }
    }

}
