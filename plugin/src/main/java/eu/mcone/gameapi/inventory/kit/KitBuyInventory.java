/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.inventory.kit;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KitBuyInventory extends CoreInventory {

    public KitBuyInventory(Player p, GamePlayer gp, GameKitManager manager, Kit kit, Runnable onBackClick) {
        super("§8» §3Kit kaufen", p, 27, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(4, ItemBuilder.wrap(kit.getItem()).lore("", "§7§oKostet: §f§o" + kit.getCoinsPrice() + " Coins").create());
        setItem(21, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5).displayName("§a§lKit kaufen").lore("", "§8» §a§nRechtsklick§8 | §7§oKaufen").create(), e -> {
            gp.buyKit(kit);
            p.closeInventory();
        });
        setItem(23, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14).displayName("§c§lAbbrechen").lore("", "§8» §c§nRechtsklick§8 | §7§oAbbrechen").create(), e -> {
            new KitsInventory(p, manager, onBackClick);
            Sound.error(p);
        });

        openInventory();
        Sound.click(p);
    }

}
