/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.kit;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class KitSortInventory extends CoreInventory {

    KitSortInventory(Player p, GamePlayer gp, GameKitManager manager, Kit kit, Runnable onBackClick) {
        super("§8» " + kit.getName()+" §8| §7Kit sortieren", p, InventorySlot.ROW_3);

        Map<Integer, ItemStack> items = manager.calculateItems(kit, p);
        for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
            if (item.getKey() < 9) {
                setItem(item.getKey(), item.getValue());
            }
        }

        for (int i = InventorySlot.ROW_2_SLOT_1; i <= InventorySlot.ROW_3_SLOT_9; i++) {
            setItem(i, PLACEHOLDER_ITEM);
        }

        setItem(InventorySlot.ROW_3_SLOT_1, new ItemBuilder(Material.INK_SACK, 1, DyeColor.LIME.getDyeData()).displayName("§a§lZurück und Speichern").create(), e -> {
            Map<ItemStack, Integer> changedItems = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                changedItems.put(getInventory().getItem(i), i);
            }

            gp.modifyKit(kit, changedItems);
            new KitsInventory(p, manager, onBackClick);
        });

        ModifiedKit modifiedKit = gp.getModifiedKit(kit.getName());
        if (modifiedKit != null) {
            setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.WATCH).displayName("§7Zuletzt bearbeitet:").lore("§7vor §f§l" + getUpdateDate(modifiedKit.getLastUpdated())).create());
        } else {
            setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.WATCH).displayName("§7Noch nie bearbeitet").create());
        }

        setItem(InventorySlot.ROW_3_SLOT_9, new ItemBuilder(Material.BARRIER).displayName("§c§lAbbrechen").create(), e -> new KitsInventory(p, manager, onBackClick));

        openInventory();
    }

    private String getUpdateDate(long lastModified) {
        String date = "NAN";
        Calendar current = Calendar.getInstance(TimeZone.getTimeZone("CEST"));

        long difference = current.getTimeInMillis() - (lastModified * 1000);
        Calendar differenceDate = Calendar.getInstance(TimeZone.getTimeZone("CEST"));
        differenceDate.setTimeInMillis(difference);

        if (differenceDate.get(Calendar.HOUR) >= 1) {
            date = "§f§l" + differenceDate.get(Calendar.HOUR) + " Stunden";
        } else if (differenceDate.get(Calendar.MINUTE) >= 1) {
            date = "§f§l" + differenceDate.get(Calendar.MINUTE) + " Minuten";
        } else if (differenceDate.get(Calendar.SECOND) >= 1) {
            date = "§f§l" + differenceDate.get(Calendar.SECOND) + " Sekunden";
        }

        return date;
    }

}
