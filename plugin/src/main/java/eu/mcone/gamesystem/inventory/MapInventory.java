/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.util.ItemBuilder;
import eu.mcone.gamesystem.api.manager.map.MapItem;
import eu.mcone.gamesystem.manager.map.MapManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MapInventory extends CoreInventory {

    private final MapManager mapManager;
    private final Class<? extends CoreInventory> returnInventory;

    public MapInventory(MapManager mapManager, Player player, Class<? extends CoreInventory> returnInventory) {
        super("§8» §f§fVoting §8| §7Map", player, InventorySlot.ROW_2, Option.FILL_EMPTY_SLOTS);

        this.mapManager = mapManager;
        this.returnInventory = returnInventory;

        int i = 0;
        for (MapItem mapItems : this.mapManager.getMaps()) {
            if (mapItems.isUse()) {
                setItem(i, new ItemBuilder(mapItems.getMaterial(), 1).displayName(mapItems.getDisplayname()).lore(mapItems.getLore()).create(), this::regsiterFunction);
                i++;
            }
        }

        setItem(18, new ItemBuilder(Material.BARRIER, 1).displayName("§7§l↩ Zurück").create());

        openInventory();
    }


    private void regsiterFunction(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        try {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7§l↩ Zurück")) {
                returnInventory.newInstance();
            } else {
                for (MapItem items : mapManager.getMaps()) {
                    if (items.getDisplayname().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())) {
                        this.mapManager.getMapVoting().put(p, items.getName());

                        if (this.mapManager.getMapPopularity().containsKey(items.getName())) {
                            this.mapManager.getMapPopularity().put(items.getName(), this.mapManager.getMapPopularity().get(items.getName()) + 1);
                        } else {
                            this.mapManager.getMapPopularity().put(items.getName(), 1);
                        }

                        update(e);
                        p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
                        mapManager.getInstance().getMessager().send(p, "§7Du hast für die map §b" + items.getName() + " §7gevotet.");
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    private void update(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int i = 0;
        for (MapItem mapItems : this.mapManager.getMaps()) {
            if (mapItems.isUse()) {
                setItem(i, new ItemBuilder(mapItems.getMaterial(), mapManager.getMapPopularity().get(mapItems.getName())).displayName(mapItems.getDisplayname()).lore(mapItems.getLore()).create());
                i++;
            }
        }

        p.updateInventory();
    }
}
