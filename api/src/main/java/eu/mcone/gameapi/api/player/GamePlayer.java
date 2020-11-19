package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.stats.CoreStats;
import eu.mcone.gameapi.api.achievement.Achievement;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackSimpleItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.kit.ModifiedKit;
import eu.mcone.gameapi.api.stats.StatsHistory;
import eu.mcone.gameapi.api.team.Team;
import org.bukkit.entity.Player;

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
    BackpackSimpleItem getCurrentBackpackItem();

    void resetCurrentBackpackItem();

    void setCurrentBackpackItem(BackpackItem item, DefaultCategory category);

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

    void removeFromGame(boolean quitted);

    boolean isOnePass();

    ModifiedKit getModifiedKit(Kit kit);

    ModifiedKit getModifiedKit(String name);

    boolean hasKitModified(Kit kit);

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
    default boolean setKit(Kit kit) {
        return setKit(kit, false);
    }

    boolean isAutoBuyKit();

    void setAutoBuyKit(boolean autoBuyKit);

    /**
     * sets the Kit items in a players inventory.
     * this does not check if the player already buyed the kit when using {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME}
     * to buy the kit for the user use {@link this#buyKit(Kit)}
     *
     * @param kit the kit that should be set
     * @param force if the kit should be set even if the player already uses this kit currently
     * @return true if the kit was set (only if he has another kit or no kit before or force == true). false otherwise
     */
    boolean setKit(Kit kit, boolean force);

    /**
     * sets the kit, that the player used before again.
     *
     * @param defaultKit the default Kit that will be set for free if the player did not choosed a kit before (might be null)
     */
    void setCurrentKitAgain(Kit defaultKit);

    /**
     * checks if the player has buyed this kit
     *
     * @param kit target kit
     * @return
     */
    boolean hasKit(Kit kit);

    /**
     * get the kit this player is currently using
     *
     * @return current kit of the player, null if he does not has a kit equipped
     */
    Kit getCurrentKit();

    /**
     * buys a kit for the player if he has enough coins.
     * if {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME} is used and the player already buyed this kit
     * or the kit was added to the player via {@link this#addKit(Kit)}}, no coins will be removed.
     *
     * @param kit the kit that should get buyed
     * @return true if the kit was buyed, false if the player had not enough coins
     */
    boolean buyKit(Kit kit);

    /**
     * saves a kit purchase for this player. This means if {@link this#buyKit(Kit)} is used and the target kit was added, no coins will be removed.
     * <b>NOTE</b>: This will only be saved in memory for server lifetime. After restart all added kits will be reset.
     * this is exactly how {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME} works. (adds a kit via this method on the first purchase)
     *
     * @param kit the kit that should be added
     * @return true if the kit was added, false if the kit was already added to the player before
     */
    boolean addKit(Kit kit);

    /**
     * removes a kit from the internal list. (Does only work if this kit was added before via {@link this#addKit(Kit)} or {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME} is used)
     * if a kit is removed the player loses coins if he buys this kit next time.
     * you can use this i.e. to customize the behavior of {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME}
     *
     * @param kit the kit that should be removed
     * @return true if the kit was removed from internal list, false if the kit was not added before
     */
    boolean removeKit(Kit kit);

    /**
     * Opens the Kit choose inventory.
     * Here players can buy new kits or arrange the slots of the items in their current kit.
     * if {@link eu.mcone.gameapi.api.Option#KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME} is used players only have to pay coins on the first purchase of a kit
     *
     * @param onBackClick runnable that will be called if the player clicks on the {@link eu.mcone.coresystem.api.bukkit.inventory.CoreInventory#BACK_ITEM} back item
     */
    void openKitInventory(Runnable onBackClick);

}
