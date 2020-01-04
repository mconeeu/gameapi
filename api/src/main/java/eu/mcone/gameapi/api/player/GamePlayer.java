package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface GamePlayer {

    Player bukkit();

    CorePlayer getCorePlayer();

    void addBackpackItem(String category, BackpackItem item) throws IllegalArgumentException;

    boolean hasBackpackItem(String category, BackpackItem item);

    boolean hasBackpackItem(String category, int id);

    void removeBackpackItem(String category, BackpackItem item);

    void buyBackpackItem(Player p, String category, BackpackItem item);

    void addAchievement(String name) throws IllegalArgumentException;

    Map<Achievement, Long> getAchievements();

    Map<Achievement, Long> getAchievements(Gamemode gamemode);

    void addAchievements(String... names);

    void removeAchievement(String name);

    void removeAchievements(String... names);

    boolean hasAchievement(String name);

    void modifyKit(Kit kit, Map<ItemStack, Integer> items);

    boolean hasKitModified(String name);

    boolean setKit(Kit kit);

    boolean hasKit(Kit kit);

    boolean buyKit(Kit kit);

    boolean removeKit(Kit kit);

    ModifiedKit getModifiedKit(Kit kit);

    ModifiedKit getModifiedKit(String name);
}
