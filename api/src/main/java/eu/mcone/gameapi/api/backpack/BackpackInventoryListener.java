/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack;

import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.backpack.BackpackItemRemoveEvent;
import eu.mcone.gameapi.api.event.backpack.BackpackItemSetEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

@RequiredArgsConstructor
public abstract class BackpackInventoryListener {

    @Setter
    protected static GamePlugin plugin;
    protected final Category category;

    public void click(BackpackItem item, GamePlayer gp, Player p, boolean notify) {
        if (gp.getCurrentBackpackItem() != null) {
            GamePlugin.getGamePlugin().getBackpackManager().removeCurrentBackpackItem(gp);
        }

        BackpackItemSetEvent event = new BackpackItemSetEvent(gp, category, item);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            onBackpackInventoryClick(item, gp, p, notify);
        }
    }

    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player player, boolean notify) {}

    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gp, Player p) {
        for (BackpackItem item : categoryItems) {
            if (gp.hasBackpackItem(category.getName(), item) || (!category.getName().equals("STORY_ITEMS") && p.hasPermission("system.game.items"))) {
                if (!plugin.getBackpackManager().getDisabledItems().contains(item.getName())) {
                    inv.addItem(item.getItem(), e -> {
                        if (gp.isEffectsVisible()) {
                            click(item, gp, p, true);
                        } else {
                            p.closeInventory();
                            Msg.send(p, "??4Du kannst keine Effekte benutzen, da Effekte von anderen f??r dich unsichtbar sind");
                        }
                    });
                }
            }
        }

        if (gp.getCurrentBackpackItem() != null && gp.getCurrentBackpackItem().getCategory().name().equals(category.getName())) {
            inv.addCustomPlacedItem(
                    InventorySlot.ROW_6_SLOT_8,
                    new ItemBuilder(Material.BARRIER, 1, 0)
                            .displayName("??c??l" + category.getName() + " Item deaktivieren")
                            .lore("??7??oDeaktiviere dein", "??7??oaktuelles Rucksack", "??7??oItem.")
                            .create(),
                    e -> removeCurrentItem(gp.getCurrentBackpackItem().getBackpackItem(), gp, p, true)
            );
        }
    }

    public void removeCurrentItem(BackpackItem item, GamePlayer gp, Player p, boolean message) {
        BackpackItemRemoveEvent event = new BackpackItemRemoveEvent(
                gp,
                category,
                item,
                GamePlugin.getGamePlugin().hasOption(Option.BACKPACK_MANAGER_AUTO_SET_RANK_BOOTS)
        );
        Bukkit.getPluginManager().callEvent(event);

        gp.resetCurrentBackpackItem();
        p.closeInventory();

        onItemItemRemove(item, gp, p);
        if (message) {
            plugin.getMessenger().sendSuccess(p, "Du hast das " + getCategoryLabel() + " Item ![" + item.getName() + "] zur??ck in deinem Rucksack gelegt!");
        }

        if (event.isApplyRankBoots()) {
            GamePlugin.getGamePlugin().getBackpackManager().setRankBoots(p);
        }
    }

    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {}

    private String getCategoryLabel() {
        return ChatColor.stripColor(category.getItemStack().getItemMeta().getDisplayName());
    }

}
