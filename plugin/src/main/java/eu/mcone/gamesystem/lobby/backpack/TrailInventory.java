/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import eu.mcone.lobby.api.LobbyPlugin;
import eu.mcone.lobby.api.enums.Category;
import eu.mcone.lobby.api.enums.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class TrailInventory extends BackpackInventory {

    public TrailInventory(Player p) {
        super(p);

        for (Item item : Item.values()) {
            if (item.hasCategory() && item.getCategory().equals(Category.TRAIL)) {
                if (playerHasItem(item)) {
                    addItem(item.getItemStack(), e -> {
                        GameTemplate.getInstance().getTrailManager().setTrail(p, item);
                        p.closeInventory();
                        p.sendMessage("§8[§7§l!§8] §fRucksack §8» §7Du hast den Trail " + item.getName() + "§7 aktiviert!");
                    });
                }
            }
        }

        setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER, 1, 0).displayName("§c§lTrail deaktivieren").lore("§7§oFalls du einen deiner Trails", "§7§oaktiviert hast, kannst Du ihn", "§7§ohiermit deaktivieren.").create(), e -> {
            GameTemplate.getInstance().getTrailManager().removeTrail(p);
            LobbyPlugin.getInstance().getMessager().send(p, "§cDu hast dein aktuellen Trail erfolgreich deaktiviert!");
        });
    }

}
