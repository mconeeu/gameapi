/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.gamesystem.api.enums.Category;
import eu.mcone.gamesystem.api.enums.Item;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import org.bukkit.entity.Player;

public class GadgetInventory extends BackpackInventory {

    public GadgetInventory(Player p) {
        super(p);

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.GADGET) && playerHasItem(item)) {
                addItem(item.getItemStack(), e -> {
                    if (playerHasItem(item)) {
                        if (p.hasPermission("lobby.silenthub")) {
                            p.getInventory().setItem(3, item.getItemStack());
                        } else {
                            p.getInventory().setItem(2, item.getItemStack());
                        }
                    }
                });
            }
        }
    }

}

