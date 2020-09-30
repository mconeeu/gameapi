package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HotbarItem {

    public static final ItemStack QUIT = new ItemBuilder(Material.IRON_DOOR, 1, 0)
            .displayName("§4§lVerlassen §8» §7§overlasse die Runde.")
            .create();

    public static final ItemStack BACKPACK = new ItemBuilder(Material.STORAGE_MINECART, 1, 0)
            .displayName("§3§lRucksack §8» §7§oZeige deine gesammelten Items an")
            .create();

    public static final ItemStack CHOOSE_TEAM = new ItemBuilder(Material.BED, 1, 0)
            .displayName("§c§lTeam §8» §7§owähle dein Team aus.")
            .create();

    public static final ItemStack CHOOSE_KIT = new ItemBuilder(Material.CHEST, 1, 0)
            .displayName("§c§lTemporärekits §8» §7§okaufe dir hier dein Temporäres Ingame Kit.")
            .create();

}
