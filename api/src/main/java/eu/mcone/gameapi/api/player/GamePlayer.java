package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.stats.CoreStats;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackSimpleItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.stats.StatsHistory;
import eu.mcone.gameapi.api.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface GamePlayer {

    Player bukkit();

    GamePlayerState getState();

    StatsHistory getStatsHistory();

    CoreStats getStats();

    int getRoundKills();

    int getRoundDeaths();

    int getRoundGoals();

    Team getTeam();

    CorePlayer getCorePlayer();

    GamePlayerSettings getSettings();

    /*
     * Backpack System
     */
    BackpackSimpleItem getLastUsedBackPackItem();

    void setLastUsedBackPackItemInventar();

    void setLastUsedBackPackItem(BackpackItem item, String category);

    void removeLastUsedBackPackItem();

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

    void resetEffectsVisible();

    void addAchievement(String name) throws IllegalArgumentException;

    Map<Achievement, Long> getAchievements();

    Map<Achievement, Long> getAchievements(Gamemode gamemode);

    void addAchievements(String... names);

    void removeAchievement(String name);

    void removeAchievements(String... names);

    boolean hasAchievement(String name);

    void setState(GamePlayerState state);

    boolean isInCameraMode();

    void setInCameraMode(Player target);

    void removeFromCameraMode();

    void changeTeamTo(Team team);

    void removeFromGame();

    boolean isOnePass();

    void buyOnePass(boolean premium);

    int getOneLevel();

    int getOneXp();

    void setOnePassLevel(int level);

    void addOnePassLevel(int level);

    void removeOnePassLevel(int level);

    void setOnePassXp(int xp);

    void addOnePassXp(int xp);

    void removeOnePassXp(int xp);

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
     *
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
