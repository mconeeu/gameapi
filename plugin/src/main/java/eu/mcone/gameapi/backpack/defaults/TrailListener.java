package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.handler.GameTrailHandler;
import lombok.Getter;
import org.bukkit.entity.Player;

public class TrailListener extends BackpackInventoryListener {

    @Getter
    private static final GameTrailHandler handler = new GameTrailHandler(plugin);

    public TrailListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.TRAIL);

        handler.setTrail(p, item);
        p.closeInventory();

        if (notify) {
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 aktiviert!");
        }
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);
        handler.removeTrail(p);
    }

}
