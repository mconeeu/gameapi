/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.inventory.inventories;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.map.GameMapItem;
import eu.mcone.gamesystem.game.manager.map.MapManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MapInventory extends CoreInventory {

    private MapManager mapManager;

    public MapInventory() {
       super(InventorySlot.ROW_2, Option.CAN_MODIFY, Option.FILL_EMPTY_SLOTS);
    }

    public void createInventory(Player player, MapManager mapManager) {
        setTitle("§8» §c§lVoting §8┋ §f§oMapvoting");

        if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_MAP_MANAGER)) {
            this.mapManager = mapManager;

            int i = 0;
            for (GameMapItem mapItems : this.mapManager.getGameWorldItems()) {
                if (mapItems.isUseMap()) {
                    setItem(i, new ItemBuilder(mapItems.getMaterial(), 1).displayName(mapItems.getDisplayName()).lore(mapItems.getLore()).create(), e -> {
                        Player p = (Player) e.getWhoClicked();

                        for (GameMapItem items : mapManager.getGameWorldItems()) {
                            if (items.getDisplayName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())) {
                                this.mapManager.getMapVoting().put(p, items.getWorld());

                                if (this.mapManager.getMapPopularity().containsKey(items.getWorld())) {
                                    this.mapManager.getMapPopularity().put(items.getWorld(), this.mapManager.getMapPopularity().get(items.getWorld()) + 1);
                                } else {
                                    this.mapManager.getMapPopularity().put(items.getWorld(), 1);
                                }

                                update(e);
                                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
                                mapManager.getInstance().getMessager().send(p, "§7Du hast für die map §b" + items.getWorld() + " §7gevotet.");
                            }
                        }
                    });
                    i++;
                }
            }

            setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create());

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            this.openInventory(player);
        } else {
            GameTemplate.getInstance().getMessager().send(player, "§cDu kannst das Mapvoting Inventory nicht benutzen, dar das MapManager Modul nicht aktiviert wurde!");
        }
    }

    private void update(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int i = 0;
        for (GameMapItem mapItems : this.mapManager.getGameWorldItems()) {
            if (mapItems.isUseMap()) {
                setItem(i, new ItemBuilder(mapItems.getMaterial(), getSize(mapItems)).displayName(mapItems.getDisplayName()).lore(mapItems.getLore()).create());
                i++;
            }
        }

        p.updateInventory();
    }

    private int getSize(final GameMapItem mapItem) {
        return mapManager.getMapPopularity().getOrDefault(mapItem.getWorld(), 0);
    }
}
