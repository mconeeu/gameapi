package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamDefinition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface GamePlayer {

    Player bukkit();

    int getRoundKills();

    int getRoundDeaths();

    int getRoundGoals();

    Team getTeam();

    CorePlayer getCorePlayer();

    GamePlayerSettings getSettings();

    void addBackpackItem(String category, BackpackItem item) throws IllegalArgumentException;

    boolean hasBackpackItem(String category, BackpackItem item);

    boolean hasBackpackItem(String category, int id);

    void removeBackpackItem(String category, BackpackItem item);

    void buyBackpackItem(Player p, String category, BackpackItem item);

    boolean isEffectsVisible();

    void setEffectsVisible(boolean visible);

    boolean hasDefaultItem(DefaultItem item);

    void addDefaultItem(DefaultItem item);

    void removeDefaultItem(DefaultItem item);

    void addAchievement(String name) throws IllegalArgumentException;

    Map<Achievement, Long> getAchievements();

    Map<Achievement, Long> getAchievements(Gamemode gamemode);

    void addAchievements(String... names);

    void removeAchievement(String name);

    void removeAchievements(String... names);

    boolean hasAchievement(String name);

    //Team
    void setTeam(Team team);

    void setTeam(TeamDefinition team);

    void removeTeam();

    void removeFromGame();

    void addKills(final int var);

    void addDeaths(final int var);

    void addGoals(final int var);

    void addLose(final int var);

    double getRoundKD();

    //Kit
    void modifyKit(Kit kit, Map<ItemStack, Integer> items);

    boolean hasKitModified(String name);

    boolean setKit(Kit kit);

    /**
     * sets the kit, that the player has buyed. If no Kit was buyed set the default kit (given as argument)
     * @param defaultKit the default Kit that will be set for free if the player didnt choose any kit
     */
    void setChoosedKit(Kit defaultKit);

    boolean hasKit(Kit kit);

    Kit getCurrentKit();

    boolean buyKit(Kit kit);

    boolean addKit(Kit kit);

    boolean removeKit(Kit kit);

    void openKitInventory(Runnable onBackClick);

    ModifiedKit getModifiedKit(Kit kit);

    ModifiedKit getModifiedKit(String name);
}
