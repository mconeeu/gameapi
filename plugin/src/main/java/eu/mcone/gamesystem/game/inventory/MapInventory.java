/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.map.GameMap;
import eu.mcone.gamesystem.game.manager.map.MapVotingHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MapInventory extends CoreInventory {

    private MapVotingHandler mapVotingHandler;

    public MapInventory(Player player, MapVotingHandler mapVotingHandler) {
        super("§8» §c§lVoting §8┋ §f§oMapvoting", player, InventorySlot.ROW_2, InventoryOption.FILL_EMPTY_SLOTS);

        if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_MAP_MANAGER)) {
            this.mapVotingHandler = mapVotingHandler;

            int i = 0;
            for (GameMap gameMap : mapVotingHandler.getMapManager().getGameMaps()) {
                if (gameMap.isUse()) {
                    setItem(i, new ItemBuilder(gameMap.getMapItem().getMaterial(), 1).displayName(gameMap.getMapItem().getDisplayName()).lore(gameMap.getMapItem().getLore()).create(), e -> {
                        Player p = (Player) e.getWhoClicked();

                        for (GameMap items : mapVotingHandler.getMapManager().getGameMaps()) {
                            if (items.getMapItem().getDisplayName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())) {
                                this.mapVotingHandler.getMapVoting().put(p, items.getWorld());

                                if (this.mapVotingHandler.getMapPopularity().containsKey(items.getWorld())) {
                                    this.mapVotingHandler.getMapPopularity().put(items.getWorld(), this.mapVotingHandler.getMapPopularity().get(items.getWorld()) + 1);
                                } else {
                                    this.mapVotingHandler.getMapPopularity().put(items.getWorld(), 1);
                                }

                                update(e);
                                p.playSound(p.getLocation(), Sound.NOTE_BASS, 1, 1);
                                mapVotingHandler.getMapManager().getCoreInstance().getMessager().send(p, "§7Du hast für die map §b" + items.getWorld() + " §7gevotet.");
                            }
                        }
                    });
                    i++;
                }
            }

            setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create());

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            openInventory();
        } else {
            GameTemplate.getInstance().getMessager().send(player, "§cDu kannst das Mapvoting Inventory nicht benutzen, dar das MapManager Modul nicht aktiviert wurde!");
        }
    }

    private void update(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int i = 0;
        for (GameMap gameMap : mapVotingHandler.getMapManager().getGameMaps()) {
            if (gameMap.isUse()) {
                setItem(i, new ItemBuilder(gameMap.getMapItem().getMaterial(), getPopularity(gameMap)).displayName(gameMap.getMapItem().getDisplayName()).lore(gameMap.getMapItem().getLore()).create());
                i++;
            }
        }

        p.updateInventory();
    }

    private int getPopularity(final GameMap mapItem) {
        return mapVotingHandler.getMapPopularity().getOrDefault(mapItem.getWorld(), 0);
    }
}
