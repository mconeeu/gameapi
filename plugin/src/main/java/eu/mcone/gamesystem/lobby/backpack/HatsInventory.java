/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.enums.Category;
import eu.mcone.gamesystem.api.enums.Item;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HatsInventory extends BackpackInventory {

    public HatsInventory(Player p) {
        super(p);

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.HAT) && playerHasItem(item)) {
                addItem(item.getItemStack(), e -> {
                    p.getInventory().setHelmet(item.getItemStack());
                    p.closeInventory();
                    p.sendMessage("§8[§7§l!§8] §fRucksack §8» §7Du hast den Kopf "+item.getName()+"§7 aufgesetzt!");
                });
            }
        }

        setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER).displayName("§c§lKopf absetzen").lore("§7§oFalls du einen deiner Köpfe", "§7§oaufgesetzt hast, kannst Du ihn", "§7§ohiermit absetzen.").create(), e -> {
            p.getInventory().setHelmet(null);
            GameTemplate.getInstance().getMessager().send(p, "§2Du hast deinen Kopf erfolgreich abgesetzt!");
        });
    }

}

