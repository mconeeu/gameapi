package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public class HatListener extends BackpackInventoryListener {

    public HatListener() {
        super("Kopf");
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.HAT);

        if (item.getId() == DefaultItem.HEAD_SECRET_STRIPCLUB.getId()) {
            p.getInventory().setHelmet(item.getItem());
            p.closeInventory();
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 aufgesetzt!");
            p.setWalkSpeed(0.25F);
        } else {
            p.getInventory().setHelmet(item.getItem());
            p.closeInventory();
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 aufgesetzt!");
            p.setWalkSpeed(0.20F);
        }
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);

        p.getInventory().setHelmet(null);
        p.setWalkSpeed(0.20F);
    }

}
