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

public class ArmorInventory extends BackpackInventory {

    public ArmorInventory(Player p) {
        super(p);
        GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.ARMOR)) {
                if (gamePlayer.hasItem(item)) {
                    addItem(item.getItemStack());
                }
            }
        }
    }

}
