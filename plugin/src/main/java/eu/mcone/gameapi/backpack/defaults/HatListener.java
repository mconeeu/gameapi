package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public class HatListener extends BackpackInventoryListener {

    public HatListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.HAT);
        p.getInventory().setHelmet(item.getItem());
        p.closeInventory();

        if (notify) {
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 aufgesetzt!");
        }

        if (item.getId() == DefaultItem.HEAD_SECRET_STRIPCLUB.getId()) {
            p.setWalkSpeed(0.25F);
        }
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);

        p.getInventory().setHelmet(null);
        p.setWalkSpeed(0.20F);
    }

}
