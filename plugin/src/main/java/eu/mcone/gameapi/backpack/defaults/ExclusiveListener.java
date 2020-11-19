package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.RankBoots;
import org.bukkit.entity.Player;

import java.util.Set;

public class ExclusiveListener extends BackpackInventoryListener {

    public ExclusiveListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {}

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gp, Player p) {
        RankBoots boots = RankBoots.getBootsByGroup(gp.getCorePlayer().getMainGroup());

        if (boots != null) {
            inv.addItem(boots.getItem());
        }
    }

}
