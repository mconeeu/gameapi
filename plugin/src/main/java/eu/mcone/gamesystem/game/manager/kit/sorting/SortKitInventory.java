/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.kit.sorting;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.api.game.manager.kit.KitItem;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public class SortKitInventory extends CoreInventory {

    SortKitInventory(Player player, Kit kit) {
        super("§8» " + kit.getDisplayName(), player, InventorySlot.ROW_1);
        GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(player.getUniqueId());
        SortKitsInventory.getSorting().put(player, kit);

        if (gamePlayer.isKitModified(kit)) {
            for (Map.Entry<String, Double> itemEntry : gamePlayer.getModifiedKit(kit).getCustomItems().entrySet()) {
                KitItem kitItem = kit.getKitItem(itemEntry.getValue());
                if (kitItem != null) {
                    setItem(Integer.valueOf(itemEntry.getKey()), kitItem.getItemStack());
                }
            }
        } else {
            int slot = 0;
            for (KitItem kitItem : kit.getKitItems()) {
                inventory.setItem(slot, kitItem.getItemStack());
                slot++;
            }
        }

        openInventory();
    }
}
