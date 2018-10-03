/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.util.ItemBuilder;
import eu.mcone.gamesystem.api.game.manager.map.MapItem;
import eu.mcone.gamesystem.game.manager.map.MapManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MapInventory extends CoreInventory {

    private MapManager mapManager;

    public MapInventory(MapManager mapManager, Player player) {
        super("§8» §c§lVoting §8┋ §f§oMapvoting", player, InventorySlot.ROW_2, Option.FILL_EMPTY_SLOTS);

        this.mapManager = mapManager;

        int i = 0;
        for (MapItem mapItems : this.mapManager.getMaps()) {
            if (mapItems.isUse()) {
                setItem(i, new ItemBuilder(mapItems.getMaterial(), 1).displayName(mapItems.getDisplayname()).lore(mapItems.getLore()).create(), this::function);
                i++;
            }
        }

        setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create());

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        openInventory();
    }


    private void function(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
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

    private void update(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int i = 0;
        for (MapItem mapItems : this.mapManager.getMaps()) {
            if (mapItems.isUse()) {
                setItem(i, new ItemBuilder(mapItems.getMaterial(), getSize(mapItems)).displayName(mapItems.getDisplayname()).lore(mapItems.getLore()).create());
                i++;
            }
        }

        p.updateInventory();
    }

    private int getSize(final MapItem mapItem) {
        return mapManager.getMapPopularity().getOrDefault(mapItem.getName(), 0);
    }
}
