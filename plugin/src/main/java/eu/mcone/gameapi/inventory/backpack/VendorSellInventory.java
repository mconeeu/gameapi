/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.backpack;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VendorSellInventory extends CoreInventory {

    VendorSellInventory(Player p, GameAPIPlayer gp, Category category, BackpackItem item) {
        super("§8» §e§lKäufer §8| §fItem", p, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        CorePlayer corePlayer = CoreSystem.getInstance().getCorePlayer(player.getUniqueId());

        setItem(InventorySlot.ROW_1_SLOT_5, item.getItem());
        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.STAINED_GLASS, 1, 13).displayName("§aVerkaufen")
                .lore("§7Verkaufe das Item " + item.getName(),
                        "§7für §a§l" + item.getSellPrice() + " §aEmeralds"
                ).create(), e -> {

            gp.removeBackpackItem(category.getName(), item);
            corePlayer.addEmeralds(item.getSellPrice());

            Msg.send(p, "§2Du hast das Item §a" + item.getName() + " §2für §f" + item.getSellPrice() + " Emeralds §2erfolgreich verkauft!");
            new BackpackSellInventory(p, category);
        });

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.STAINED_GLASS, 1, 14).displayName("§cAbbrechen").create(), e -> new BackpackSellInventory(p, category));

        openInventory();
    }
}
