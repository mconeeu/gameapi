/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import eu.mcone.lobby.api.enums.Category;
import eu.mcone.lobby.api.enums.Item;
import org.bukkit.entity.Player;

public class MaterialInventory extends BackpackInventory {

    public MaterialInventory(Player p) {
        super(p);
        GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

        if (!gamePlayer.hasItem(Item.MATERIAL_IRON_6)) {
            if (gamePlayer.hasItem(Item.MATERIAL_IRON_4) && gamePlayer.hasItem(Item.MATERIAL_IRON_2)) {
                gamePlayer.removeItem(Item.MATERIAL_IRON_4);
                gamePlayer.removeItem(Item.MATERIAL_IRON_2);

                gamePlayer.addItem(Item.MATERIAL_IRON_6);
            }
        }

        if (!gamePlayer.hasItem(Item.MATERIAL_IRON_10)) {
            if (gamePlayer.hasItem(Item.MATERIAL_IRON_6) && gamePlayer.hasItem(Item.MATERIAL_IRON_4)) {
                gamePlayer.removeItem(Item.MATERIAL_IRON_4);
                gamePlayer.removeItem(Item.MATERIAL_IRON_6);

                gamePlayer.addItem(Item.MATERIAL_IRON_10);
            }
        }

        if (!gamePlayer.hasItem(Item.MATERIAL_IRON_8)) {
            if (gamePlayer.hasItem(Item.MATERIAL_IRON_6) && gamePlayer.hasItem(Item.MATERIAL_IRON_2)) {
                gamePlayer.removeItem(Item.MATERIAL_IRON_2);
                gamePlayer.removeItem(Item.MATERIAL_IRON_6);

                gamePlayer.addItem(Item.MATERIAL_IRON_8);
            }
        }

        if (!gamePlayer.hasItem(Item.MATERIAL_IRON_10)) {
            if (gamePlayer.hasItem(Item.MATERIAL_IRON_8) && gamePlayer.hasItem(Item.MATERIAL_IRON_2)) {
                gamePlayer.removeItem(Item.MATERIAL_IRON_8);
                gamePlayer.removeItem(Item.MATERIAL_IRON_2);

                gamePlayer.addItem(Item.MATERIAL_IRON_10);
            }
        }

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.MATERIAL)) {
                if (gamePlayer.hasItem(item)) {
                    addItem(item.getItemStack(), e -> {
                    });
                }
            }
        }
    }

}
