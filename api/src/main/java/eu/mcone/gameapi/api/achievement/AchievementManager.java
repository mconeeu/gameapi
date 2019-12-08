package eu.mcone.gameapi.api.achievement;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import org.bukkit.entity.Player;

public interface AchievementManager {

    void registerAchievements(Achievement... achievements);

    void reload();

    Achievement getAchievement(String name);

    Achievement getAchievement(Gamemode gamemode, String name);

    boolean setAchievement(GameAPIPlayer<?> player, Achievement achievement);

    void openAchievementInventory(Player p);

    void openAchievementInventory(Player p, Gamemode gamemode);
}
