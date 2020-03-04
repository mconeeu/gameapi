package eu.mcone.gameapi.inventory.backpack.trade;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TradeChooseInventory extends CategoryInventory {

    @Setter
    protected static GamePlugin plugin;

    protected final GameAPIPlayer gamePlayer;
    private final Map<String, Set<BackpackItem>> ownItems;

    public TradeChooseInventory(Player p, Category category) {
        super("§8» §3§lWähle aus", p, category.getItemStack());
        this.gamePlayer = GameAPIPlugin.getSystem().getGamePlayer(p.getUniqueId());
        this.ownItems = gamePlayer.getBackpackItems();

        Map<Integer, Category> sortedCategories = new HashMap<>();
        for (Category cat : ((GameBackpackManager) plugin.getBackpackManager()).getCategories()) {
            sortedCategories.put(cat.getSort(), cat);
        }

        for (Category cat : sortedCategories.values()) {
            if (cat.isSellable()) {
                addCategory(cat.getItemStack());
            }
        }

        for (BackpackItem item : ((GameBackpackManager) plugin.getBackpackManager()).getCategoryItems(category.getName())) {
            if (hasItem(category.getName(), item) && item.isSellable()) {
                addItem(item.getItem(), e -> {
                    ((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().getChoosedItems().put(p, item);

                    Player partner = ((GameBackpackManager) plugin.getBackpackManager()).getTradeManager().getTraidingPartner(p);
                    new TradingFinishInventory(gamePlayer, item, ((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().getChoosedItems().getOrDefault(partner, null), false);
                });
            }
        }

        openInventory();
    }

    private boolean hasItem(String category, BackpackItem item) {
        return ownItems.getOrDefault(category, Collections.emptySet()).contains(item);
    }

    @Override
    protected void openCategoryInventory(ItemStack itemStack, Player player) {
        new TradeChooseInventory(player, ((GameBackpackManager) plugin.getBackpackManager()).getCategoryByItemDisplayName(itemStack.getItemMeta().getDisplayName())).openInventory();
    }

}
