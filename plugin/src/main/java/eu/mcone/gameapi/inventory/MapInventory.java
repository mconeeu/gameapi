/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.map.GameMapVotingHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class MapInventory extends CoreInventory {

    public static final String TITLE = "§8» §c§lVoting §8┋ §f§oMapvoting";

    public MapInventory(Player p, GameMapVotingHandler mapVotingHandler, CoreInventory coreInventory) {
        super(TITLE, p, InventorySlot.ROW_2, InventoryOption.FILL_EMPTY_SLOTS);

        int i = 0;
        for (Map.Entry<GameAPIMap, List<Player>> entry : mapVotingHandler.getPopularityMap().entrySet()) {
            setItem(
                    i,
                    new ItemBuilder(entry.getKey().getItem())
                            .displayName("§f§o" + entry.getKey().getWorld().getName())
                            .lore("§7§o" + entry.getValue().size() + " Votes")
                            .create(),
                    e -> {
                        mapVotingHandler.vote(p, entry.getKey(), coreInventory);

                        p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
                        mapVotingHandler.getMapManager().getSystem().getMessenger().send(p, "§7Du hast für die Map §f§l" + entry.getKey().getName() + " §7 gevotet.");
                    });
            i++;
        }

        if (coreInventory != null) {
            setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create(), e -> coreInventory.openInventory());
        }

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        openInventory();
    }

}
