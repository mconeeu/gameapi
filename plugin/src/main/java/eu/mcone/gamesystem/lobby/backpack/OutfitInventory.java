/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import eu.mcone.gamesystem.lobby.manager.OutfitFactory;
import eu.mcone.lobby.api.enums.Category;
import eu.mcone.lobby.api.enums.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OutfitInventory extends BackpackInventory {

    public OutfitInventory(Player p) {
        super(p);

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.OUTFITS) && playerHasItem(item)) {
                addItem(item.getItemStack(), e -> {
                    OutfitFactory.setOutfit(p, item);
                    p.closeInventory();
                    p.sendMessage("§8[§7§l!§8] §fRucksack §8» §7Du hast das Outfit " + item.getName() + "§7 angezogen!");
                });
            }
        }

        setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER).displayName("§c§lOutfit ausziehen").lore("§7§oFalls du eines deiner Outfits", "§7§oangezogen hast, kannst Du es", "§7§ohiermit ausziehen.").create(), e -> {
            p.getInventory().setArmorContents(null);
            p.sendMessage("§8[§7§l!§8] §fRucksack §8» §7Du hast dein Outfit erfolgreich ausgezogen!");
            p.closeInventory();
        });
    }

}
