/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.enums;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum Category {

    STORY_ITEMS(0, "Story-Items", new ItemBuilder(Material.BOOK, 1, 0).displayName("§c§lStory-Items").lore("§7§oHier findest Du alle Items,", "§7§odie Du für die Story benötigst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    TRAIL(1, "Trails", new ItemBuilder(Material.BLAZE_POWDER, 1, 0).displayName("§6§lTrails").lore("§7§oTrails sind Spuren, die Du", "§7§ohinter Dir herziehst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    GADGET(2, "Gadgets", new ItemBuilder(Material.REDSTONE, 1, 0).displayName("§4§lGadgets").lore("§7§oGadgets sind Items, mit denen", "§7§oDu coole Dinge machen kannst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    HAT(3, "Hüte", new ItemBuilder(Material.DIAMOND_HELMET, 1, 0).displayName("§b§lKöpfe").lore("§7§oHier kannst Du alle möglichen", "§7§oKöpfe sammeln und aufziehen", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    OUTFITS(4, "Outfits", new ItemBuilder(Material.LEATHER_CHESTPLATE, 1, 0).displayName("§e§lOutfits").lore("§7§oHier befinden sich Deine Outfits,", "§7§odie Du auch anziehen kannst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    ANIMAL(5, "Tiere", new ItemBuilder(Material.MONSTER_EGG, 1, 55).displayName("§a§lTiere").lore("§7§oHaustiere folgen Dir wohin", "§7§oDu gehst", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    ARMOR(6, "Rüstung", new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1, 0).displayName("§d§lRüstung").lore("§7§oHier befindet sich deine Rüstung", "§7§ofür deine Gang Kämpfe", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").unbreakable(true).itemFlags(ItemFlag.HIDE_ATTRIBUTES).create()),
    MATERIAL(7, "Materialien", new ItemBuilder(Material.DIAMOND, 1, 0).displayName("§9§lMaterial").lore("§7§oHier befindet sich deine Materialien", "§7§ozum Items herstellen", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    EXCLUSIVE(8, "Exklusive Items", new ItemBuilder(Material.GOLD_HOE, 1, 0).displayName("§c§lExklusive Items").lore("§7§oHier befindet sich deine Exklusiven Items", "§7§owie Event oder Rang Items", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create()),
    INGAME(9, "Ingame Items", new ItemBuilder(Material.IRON_SWORD, 1, 0).displayName("§b§lIngame Items").lore("§7§oHier befinden sich deine gewonnen Ingame Items", "§7§ofür jeden SpielModi", "", "§8» §f§nLinksklick§8 | §7§oÖffnen").create());

    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private ItemStack item;

    Category(int id, String name, ItemStack item) {
        this.id = id;
        this.name = name;
        this.item = item;
    }

}
