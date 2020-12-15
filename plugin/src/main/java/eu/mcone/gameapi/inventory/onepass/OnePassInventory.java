/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.onepass;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.onepass.OnePassLevelAward;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class OnePassInventory extends CoreInventory {

    public OnePassInventory(Player p, int page) {
        this(p, GameAPI.getInstance().getGamePlayer(p), page);
    }

    public OnePassInventory(Player p, GamePlayer gp, int page) {
        super("§fOnePass | Dein Level: " + gp.getOneLevel() + "." + (gp.getOneXp() + 1), p, InventorySlot.ROW_6);

        setItem(InventorySlot.ROW_1_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_2, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_7, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_8, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_1_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());

        setItem(InventorySlot.ROW_4_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_2, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_4, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_5, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_6, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_7, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_8, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_9, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());

        setItem(InventorySlot.ROW_1_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 8).displayName("§8//§oMCONE§8//").create());
        setItem(InventorySlot.ROW_4_SLOT_1, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 8).displayName("§8//§oMCONE§8//").create());

        int startLevel = (page * 8) + 1;

        Map<Integer, OnePassLevelAward> awards = new HashMap<>();
        Map<Integer, OnePassLevelAward> freeAwards = new HashMap<>();
        for (OnePassLevelAward award : OnePassLevelAward.values()) {
            if (award.getLevel() >= startLevel && award.getLevel() < (startLevel + 8)) {
                if (!award.isFreeAward()) {
                    awards.put(award.getLevel(), award);
                } else {
                    freeAwards.put(award.getLevel(), award);
                }
            }
        }

        for (int i = InventorySlot.ROW_2_SLOT_2, x = startLevel; i <= InventorySlot.ROW_2_SLOT_9; i++, x++) {
            if (awards.containsKey(x)) {
                OnePassLevelAward award = awards.get(x);
                ItemBuilder item = ItemBuilder.wrap(award.getBackpackItem().getItem()).addLore("§fStufe: " + award.getLevel());


                setItem(
                        i,
                        item.create(),
                        e -> Sound.done(p)
                );

            } else {
                break;
            }
        }
        for (int i = InventorySlot.ROW_3_SLOT_2, x = startLevel; i <= InventorySlot.ROW_3_SLOT_9; i++, x++) {
            OnePassLevelAward award = freeAwards.get(x);
            if (award != null) {
                ItemBuilder item = ItemBuilder.wrap(award.getBackpackItem().getItem()).addLore("§fStufe: " + award.getLevel());


                setItem(
                        i,
                        item.create(),
                        e -> Sound.done(p)
                );
            }
        }

        for (int i = InventorySlot.ROW_1_SLOT_2, x = startLevel; i <= InventorySlot.ROW_1_SLOT_9; i++, x++) {
            if (awards.containsKey(x)) {
                OnePassLevelAward award = awards.get(x);
                if (gp.getOneLevel() >= award.getLevel()) {
                    setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8//§oMCONE§8//").create());
                }
            }
        }
        for (int i = InventorySlot.ROW_4_SLOT_2, x = startLevel; i <= InventorySlot.ROW_4_SLOT_9; i++, x++) {
            if (awards.containsKey(x)) {
                OnePassLevelAward award = awards.get(x);
                if (gp.getOneLevel() >= award.getLevel()) {
                    setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 13).displayName("§8//§oMCONE§8//").create());
                }
            }
        }


        setItem(
                InventorySlot.ROW_6_SLOT_6,
                CategoryInventory.RIGHT_ITEM,
                e -> {
                    int maxLevel = 0;
                    for (OnePassLevelAward award : OnePassLevelAward.values()) {
                        if (award.getLevel() > maxLevel) {
                            maxLevel = award.getLevel();
                        }
                    }

                    if ((page + 1) * 8 <= maxLevel - 1) {
                        new OnePassInventory(p, gp, page + 1);
                    } else {
                        Sound.error(p);
                    }
                }
        );
        setItem(
                InventorySlot.ROW_6_SLOT_5,
                CategoryInventory.LEFT_ITEM,
                e -> {
                    if (page - 1 >= 0) {
                        new OnePassInventory(p, gp, page - 1);
                    } else {
                        Sound.error(p);
                    }
                }
        );


        setItem(InventorySlot.ROW_2_SLOT_1, new ItemBuilder(Material.BOOK, 1, 0).displayName("§3§lOnePass §3Belohnungen:").create());
        setItem(InventorySlot.ROW_3_SLOT_1, new ItemBuilder(Material.PAPER, 1, 0).displayName("§3§lFreePass §3Belohnungen:").create());

        setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.EXP_BOTTLE, 1).displayName("§aDeine XP:").lore(
                "§3" + (gp.getOneXp() + 1) + " Xp",
                "§7§oDir fehlen noch §f" + (OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL - gp.getOneXp() - 1) + " Xp",
                "§7§ofür die nächste Stufe!",
                "",
                getXpProgess(gp.getOneXp())
        ).create(), e -> {
            Sound.done(p);
        });
        setItem(InventorySlot.ROW_6_SLOT_9, new ItemBuilder(Material.EMERALD, 1).displayName("§aDeine Stufen:").lore(
                "§3" + gp.getOneLevel() + " Stufen",
                "",
                "§fPreis: §2" + OnePassManager.BUY_NEW_LEVEL_PRICE + " Emeralds",
                "§f§nRechtsklick§8 | §7§oStufen kaufen"
        ).create(), e -> {
            if (gp.getCorePlayer().getEmeralds() >= OnePassManager.BUY_NEW_LEVEL_PRICE) {
                gp.getCorePlayer().removeEmeralds(OnePassManager.BUY_NEW_LEVEL_PRICE);
                gp.addOnePassLevel(1);
                GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(p);
            }

            Sound.done(p);
        });

        setItem(InventorySlot.ROW_6_SLOT_3, new ItemBuilder(Material.BOOK, 1).displayName("§eOnePass Quests").create(), e -> new QuestInventory(p));


        setItem(InventorySlot.ROW_6_SLOT_1, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create(), e -> p.closeInventory());
        if (!gp.isOnePass()) {
            setItem(InventorySlot.ROW_6_SLOT_2, new ItemBuilder(Material.GOLD_INGOT, 1, 0).displayName("§3OnePass kaufen").lore("", "§8» §a§nRechtsklick§8 | §7§oÖffnen").create(), e -> {
                new OnePassBuyInventory(p);
            });
        }

        openInventory();
    }

    private static String getXpProgess(int xp) {
        StrBuilder sb = new StrBuilder();
        for (int i = 0; i < OnePassManager.NEEDED_XP_FOR_NEXT_LEVEL; i++) {
            sb.append(i <= xp ? ("§a" + "█") : ("§7█"));
        }

        return sb.toString();
    }

}
