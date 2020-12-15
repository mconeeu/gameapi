/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.handler.GameOutfitHandler;
import lombok.Getter;
import org.bukkit.entity.Player;

public class OutfitListener extends BackpackInventoryListener {

    @Getter
    private static final GameOutfitHandler handler = new GameOutfitHandler();

    public OutfitListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.OUTFIT);

        handler.setOutfit(p, item);
        p.closeInventory();

        if (notify) {
            plugin.getMessenger().send(p, "§7Du hast das §f" + item.getName() + "§7 angezogen!");
        }
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);
        p.getInventory().setArmorContents(null);
    }

}
