package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum TeamDefinition {

    RED(1, "red", "§cRot", ChatColor.RED, Color.RED, new ItemBuilder(Material.WOOL, 1, 14).displayName("§cRot").create()),
    BLUE(2, "blue", "§9Blau", ChatColor.BLUE, Color.BLUE, new ItemBuilder(Material.WOOL, 1,11).displayName("§9Blau").create()),
    YELLOW(3, "yellow", "§eGelb", ChatColor.YELLOW, Color.YELLOW, new ItemBuilder(Material.WOOL, 1, 4).displayName("§eGelb").create()),
    GREEN(4, "green", "§aGrün", ChatColor.GREEN, Color.GREEN, new ItemBuilder(Material.WOOL, 1, 5).displayName("§aGrün").create()),
    ORANGE(5, "orange", "§6Orange", ChatColor.GOLD, Color.ORANGE, new ItemBuilder(Material.WOOL, 1,1).displayName("§6Orange").create()),
    AQUA(6, "aqua", "§5Türkis", ChatColor.AQUA, Color.AQUA, new ItemBuilder(Material.WOOL, 1,9).displayName("§5Türkis").create()),
    WHITE(7, "white", "§fWeiß", ChatColor.WHITE, Color.WHITE, new ItemBuilder(Material.WOOL, 1,0).displayName("§fWeiß").create()),
    PURPLE(8, "purple", "§5Lila", ChatColor.LIGHT_PURPLE, Color.PURPLE, new ItemBuilder(Material.WOOL, 1,10).displayName("§5Lila").create()),
    ERROR(9, "error", "§8» §4ERROR §8«", ChatColor.RED, Color.RED, new ItemBuilder(Material.WOOL, 1,14).displayName("§8» §4ERROR §8«").create());

    private final int position;
    private final String name;
    private final String prefix;
    private final ChatColor chatColor;
    private final Color color;
    private final ItemStack itemStack;

    TeamDefinition(int position,
                   final String team,
                   final String prefix,
                   final ChatColor chatColor,
                   final Color color,
                   final ItemStack itemStack) {

        this.position = position;
        this.name = team;
        this.prefix = prefix;
        this.chatColor = chatColor;
        this.color = color;
        this.itemStack = itemStack;
    }
}
