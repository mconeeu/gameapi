/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.map;

import eu.mcone.coresystem.api.bukkit.chat.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.map.GameMapManager;
import eu.mcone.gameapi.map.GameMapRotationHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MapInventory extends CoreInventory {


    public MapInventory(Player player, GameMapManager mapManager) {
        super("§aMap Manager", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);


        setItem(InventorySlot.ROW_2_SLOT_3, CategoryInventory.REFRESH_ITEM, e -> {
            if (mapManager.isRotationHandlerLoaded()) {
                ((GameMapRotationHandler) mapManager.getMapRotationHandler()).rotate();
                Msg.broadcast(new SimpleBroadcast("§fDie Map wird gewechselt!"));
                Msg.send(player, "§2Du hast die Map erfolgreich rotiert!");
            } else {
                Msg.send(player, "§4Der RotationHandler wurde nicht initialisiert!");
            }
            player.closeInventory();
        });

        setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.WATCH).displayName("§fMap Änderung: " + mapManager.getMapRotationHandler().getFormattedTimeUntilNextRotation()).create());

        setItem(InventorySlot.ROW_2_SLOT_7, new ItemBuilder(Material.GRASS).displayName("Aktuelle Maps").create(), e-> {
            StringBuilder sb = new StringBuilder("§7Folgende Welten sind geladen: ");
            for (GameAPIMap map : mapManager.getMaps()) {
                sb.append("§3").append(map.getName()).append("§7, ");
            }
            sb.append("\n");

            Msg.send(player, sb.toString());
            player.closeInventory();
        });


        openInventory();

    }
}
