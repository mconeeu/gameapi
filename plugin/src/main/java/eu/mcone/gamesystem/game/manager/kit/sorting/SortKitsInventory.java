/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.kit.sorting;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.game.manager.kit.KitsInventory;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SortKitsInventory extends CoreInventory {

    @Getter
    private static Map<Player, Kit> sorting = new HashMap<>();

    public SortKitsInventory(Player player) {
        super("§8» §e§lKits sortieren", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);

        int slot = KitsInventory.getStartSlot();
        for (Kit kit : GameTemplate.getInstance().getKitManager().getKits()) {
            setItem(slot, kit.getKitItem(), e -> new SortKitInventory(player, kit));
            slot++;
        }

        openInventory();
    }
}
