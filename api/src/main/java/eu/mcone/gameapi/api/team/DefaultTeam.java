package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@Getter
public enum DefaultTeam {

    RED(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Rot",
                    "§cRot",
                    1,
                    ChatColor.RED,
                    new ItemBuilder(Material.WOOL, 1, 14)
                            .displayName("§cRot")
                            .create()
            )
    ),

    BLUE(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Blau",
                    "§9Blau",
                    2,
                    ChatColor.BLUE,
                    new ItemBuilder(Material.WOOL, 1, 11)
                            .displayName("§9Blau")
                            .create()
            )
    ),

    YELLOW(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Gelb",
                    "§eGelb",
                    3,
                    ChatColor.YELLOW,
                    new ItemBuilder(Material.WOOL, 1, 4)
                            .displayName("§eGelb")
                            .create()
            )
    ),

    GREEN(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Grün",
                    "§aGrün",
                    4,
                    ChatColor.GREEN,
                    new ItemBuilder(Material.WOOL, 1, 5)
                            .displayName("§aGrün")
                            .create()
            )
    ),

    ORANGE(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Orange",
                    "§6Orange",
                    5,
                    ChatColor.GOLD,
                    new ItemBuilder(Material.WOOL, 1, 1)
                            .displayName("§6Orange")
                            .create()
            )
    ),

    AQUA(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Türkis",
                    "§5Türkis",
                    6,
                    ChatColor.AQUA,
                    new ItemBuilder(Material.WOOL, 1, 9)
                            .displayName("§5Türkis")
                            .create()
            )
    ),

    WHITE(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Weiß",
                    "§fWeiß",
                    7,
                    ChatColor.WHITE,
                    new ItemBuilder(Material.WOOL, 1, 0)
                            .displayName("§fWeiß")
                            .create()
            )
    ),

    PURPLE(
            GamePlugin.getGamePlugin().getTeamManager().createTeam(
                    "Lila",
                    "§5Lila",
                    8,
                    ChatColor.LIGHT_PURPLE,
                    new ItemBuilder(Material.WOOL, 1, 10)
                            .displayName("§5Lila")
                            .create()
            )
    );

    private final Team team;

    DefaultTeam(Team team) {
        this.team = team;
    }

}
