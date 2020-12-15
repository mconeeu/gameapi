/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.onepass;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OnePassBuyInventory extends CoreInventory {

    public OnePassBuyInventory(Player p) {
        super("§fOnePass kaufen", p, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);
        GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(p);

        setItem(InventorySlot.ROW_2_SLOT_4, new ItemBuilder(Material.EMERALD, 1, 0).displayName("§3OnePass-Standard").lore("", "§8» §fKosten: §a150 Emeralds", "§8» §a§nRechtsklick§8 | §7§oKaufen").create(), e -> {
            gamePlayer.buyOnePass(false);

            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                Sound.equip(p);
                GameAPI.getInstance().getMessenger().send(player, "§aDu hast den §3OnePass §agekauft!");

                setItem(InventorySlot.ROW_3_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                setItem(InventorySlot.ROW_2_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                setItem(InventorySlot.ROW_2_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                    Sound.done(p);

                    setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                    setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5).displayName("§aOnePass gekauft").create());
                    setItem(InventorySlot.ROW_1_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                    Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                        Sound.done(p);

                        setItem(InventorySlot.ROW_2_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_2_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_3_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_3_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_1_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_1_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                            Sound.done(p);

                            setItem(InventorySlot.ROW_2_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_3_SLOT_3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_3_SLOT_8, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                                GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(player);
                            }, 10);
                        }, 4);
                    }, 7);
                }, 12);
            }, 1);
        });


        setItem(InventorySlot.ROW_2_SLOT_6, new ItemBuilder(Material.DIAMOND, 1, 0).displayName("§3§lOnePass-Premium").lore("", "§fDu erhälst beim Kauf 10 weitere Stufen", "", "§8» §fKosten: §a225 Emeralds", "§8» §a§nRechtsklick§8 | §7§oKaufen").create(), e -> {
            gamePlayer.buyOnePass(true);

            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                Sound.equip(p);
                GameAPI.getInstance().getMessenger().send(player, "§aDu hast den §3OnePass-Premium §agekauft!");

                setItem(InventorySlot.ROW_3_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                setItem(InventorySlot.ROW_2_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                setItem(InventorySlot.ROW_2_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
                Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                    Sound.done(p);

                    setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                    setItem(InventorySlot.ROW_2_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5).displayName("§aOnePass gekauft").create());
                    setItem(InventorySlot.ROW_1_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                    Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                        Sound.done(p);

                        setItem(InventorySlot.ROW_2_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_2_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_3_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_3_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_1_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        setItem(InventorySlot.ROW_1_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 4).displayName("§8//§oMCONE§8//").create());
                        Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                            Sound.done(p);

                            setItem(InventorySlot.ROW_2_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_3_SLOT_3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_3_SLOT_8, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 2).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            setItem(InventorySlot.ROW_1_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 10).displayName("§8//§oMCONE§8//").create());
                            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                                GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(player);
                            }, 10);
                        }, 4);
                    }, 7);
                }, 12);
            }, 1);
        });

        setItem(InventorySlot.ROW_3_SLOT_9, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create(), e -> GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(player))
        ;


        openInventory();
    }
}
