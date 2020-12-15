/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum DefaultCategory {

    HAT("Hüte", "", new ItemBuilder(Material.DIAMOND_HELMET, 1, 0).displayName("§b§lKöpfe").lore("§7§oHier kannst Du alle möglichen", "§7§oKöpfe sammeln und aufziehen", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    GADGET("Gadgets", "", new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§4§lGadgets").lore("§7§oGadgets sind Items, mit denen", "§7§oDu coole Dinge machen kannst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    TRAIL("Trails", "", new ItemBuilder(Material.BLAZE_POWDER, 1, 0).displayName("§6§lTrails").lore("§7§oTrails sind Spuren, die Du", "§7§ohinter Dir herziehst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    OUTFIT("Outfits", "", new ItemBuilder(Material.LEATHER_CHESTPLATE, 1, 0).displayName("§e§lOutfits").lore("§7§oHier befinden sich Deine Outfits,", "§7§odie Du auch anziehen kannst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    PET("Tiere", "", new ItemBuilder(Material.MONSTER_EGG, 1, 55).displayName("§a§lTiere").lore("§7§oHaustiere folgen Dir wohin", "§7§oDu gehst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    EXCLUSIVE("Exklusive Items", "", new ItemBuilder(Material.GOLD_HOE, 1, 0).displayName("§c§lExklusive Items").lore("§7§oHier befindet sich deine Exklusiven Items", "§7§owie Event oder Rang Items", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),;

    private final String name;
    private final String description;
    private final ItemStack item;

    DefaultCategory(String name, String description, ItemStack item) {
        this.name = name;
        this.description = description;
        this.item = item;
    }

}
