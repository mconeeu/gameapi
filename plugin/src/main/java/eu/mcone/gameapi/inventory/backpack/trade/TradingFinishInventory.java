/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.backpack.trade;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TradingFinishInventory extends CoreInventory {

    TradingFinishInventory(GameAPIPlayer gp, BackpackItem selfChoosedItem, BackpackItem partnerChoosedItem, boolean accpeted) {
        super("§8» §e§lTrade", gp.bukkit(), InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);
        GameBackpackManager backpackManager = ((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager());
        Player p = gp.bukkit();
        Player partner = backpackManager.getTradeManager().getTraidingPartner(p);
        boolean partnerAccepted = partner != null && backpackManager.getTradeManager().getTradeItemAccepted().contains(partner);

        setItem(InventorySlot.ROW_2_SLOT_2, selfChoosedItem.getItem());

        if (accpeted) {
            setItem(InventorySlot.ROW_1_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
            setItem(InventorySlot.ROW_2_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
            setItem(InventorySlot.ROW_3_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
        }

        setItem(
                InventorySlot.ROW_2_SLOT_4,
                new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13)
                        .displayName(accpeted ? (partnerChoosedItem != null && partnerAccepted ? "§aJetzt Traden" : "§7§oWarte auf Partner") : "§aEigenes Item Akzeptieren")
                        .lore("§7Tausche das Item §f" + selfChoosedItem.getName() + (partnerChoosedItem != null ? "§7 mit " + partnerChoosedItem.getName() : ""))
                        .create(),
                e -> {
                    if (partnerChoosedItem != null && partnerAccepted) {
                        GamePlayer gpPartner = GameAPIPlugin.getSystem().getGamePlayer(partner);

                        System.out.println("selfChoosed displayname: " + selfChoosedItem.getItem().getItemMeta().getDisplayName());
                        System.out.println("selfChoosed category: " + backpackManager
                                .getCategoryByItemDisplayName(selfChoosedItem.getItem().getItemMeta().getDisplayName()));
                        System.out.println("partnerChoosed displayname: " + partnerChoosedItem.getItem().getItemMeta().getDisplayName());
                        System.out.println("partnerChoosed category: " + backpackManager
                                .getCategoryByItemDisplayName(partnerChoosedItem.getItem().getItemMeta().getDisplayName()));
                        String selfChoosedItemCategory = backpackManager
                                .getCategoryByItemDisplayName(selfChoosedItem.getItem().getItemMeta().getDisplayName()).getName();
                        String partnerChoosedItemCatergory = backpackManager
                                .getCategoryByItemDisplayName(partnerChoosedItem.getItem().getItemMeta().getDisplayName()).getName();

                        gp.removeBackpackItem(
                                selfChoosedItemCategory,
                                selfChoosedItem
                        );
                        gpPartner.removeBackpackItem(
                                partnerChoosedItemCatergory,
                                partnerChoosedItem
                        );

                        gp.addBackpackItem(
                                partnerChoosedItemCatergory,
                                partnerChoosedItem
                        );
                        gpPartner.removeBackpackItem(
                                selfChoosedItemCategory,
                                selfChoosedItem
                        );

                        GameAPIPlugin.getSystem().getMessenger().send(p, "§2Du hast das Item §a" + selfChoosedItem.getName() + " §2für §f" + partnerChoosedItem.getName() + " §2erfolgreich ausgetauscht!");
                        GameAPIPlugin.getSystem().getMessenger().send(partner, "§2Du hast das Item §a" + partnerChoosedItem.getName() + " §2für §f" + selfChoosedItem.getName() + " §2erfolgreich ausgetauscht!");
                    } else if (!accpeted) {
                        backpackManager.getTradeManager().getTradeItemAccepted().add(p);
                        new TradingFinishInventory(gp, selfChoosedItem, partnerChoosedItem, true);

                        if (partnerChoosedItem != null) {
                            new TradingFinishInventory(GameAPIPlugin.getSystem().getGamePlayer(partner), partnerChoosedItem, selfChoosedItem, partnerAccepted);
                        }
                    }
                });

        setItem(
                InventorySlot.ROW_2_SLOT_5,
                new ItemBuilder(Material.COMPASS, 1, 0)
                        .displayName("§aBist du sicher?")
                        .lore(
                                "§7Links ist das Item " + selfChoosedItem.getName(),
                                "§7das was du ausgesucht hast und was du",
                                "§7gegen das rechte Item tauschen wirst!"
                        )
                        .create());

        setItem(
                InventorySlot.ROW_2_SLOT_6,
                new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14)
                        .displayName("§cAbbrechen")
                        .create(),
                e -> backpackManager.getTradeManager().openBackpackTraidInventory(player)
        );

        setItem(
                InventorySlot.ROW_2_SLOT_8,
                partnerChoosedItem != null ?
                        partnerChoosedItem.getItem()
                        : new ItemBuilder(Material.STAINED_CLAY, 1, 9)
                        .displayName("§8§lNoch nicht ausgewählt")
                        .lore(
                                "§7§oDein Partner hat noch kein Item",
                                "§7§ozum Tauschen ausgewählt."
                        )
                        .create()
        );

        if (partnerChoosedItem != null && partnerAccepted) {
            setItem(InventorySlot.ROW_1_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
            setItem(InventorySlot.ROW_2_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
            setItem(InventorySlot.ROW_3_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8§o//MCONE//").create());
        }

        openInventory();
    }
}

