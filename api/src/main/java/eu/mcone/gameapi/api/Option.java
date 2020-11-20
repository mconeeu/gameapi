package eu.mcone.gameapi.api;

import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamManager;

public enum Option {

    /**
     * Sets all {@link HotbarItem} Items to all players on join
     * Useful for Games with a Lobby phase where backpack items like gadgets and trails should be enables
     */
    HOTBAR_SET_ITEMS,

    //BackpackManager
    /**
     * Registers all {@link DefaultCategory}s
     * This makes all categories in the linked enum available in BackpackInventory
     */
    BACKPACK_MANAGER_REGISTER_ALL_DEFAULT_CATEGORIES,
    /**
     * Registers the {@link DefaultCategory#PET} category
     */
    BACKPACK_MANAGER_REGISTER_PET_CATEGORY,
    /**
     * Registers the {@link DefaultCategory#GADGET} category
     */
    BACKPACK_MANAGER_REGISTER_GADGET_CATEGORY,
    /**
     * Registers the {@link DefaultCategory#HAT} category
     */
    BACKPACK_MANAGER_REGISTER_HAT_CATEGORY,
    /**
     * Registers the {@link DefaultCategory#OUTFIT} category
     */
    BACKPACK_MANAGER_REGISTER_OUTFIT_CATEGORY,
    /**
     * Registers the {@link DefaultCategory#TRAIL} category
     */
    BACKPACK_MANAGER_REGISTER_TRAIL_CATEGORY,
    /**
     * Registers the {@link DefaultCategory#EXCLUSIVE} category
     */
    BACKPACK_MANAGER_REGISTER_EXCLUSIVE_CATEGORY,
    /**
     * Automattically sets any player boots in color of their main rank as shoes on join
     */
    BACKPACK_MANAGER_AUTO_SET_RANK_BOOTS,

    //KitManager
    /**
     * Clears the whole inventory if a new kit is set (i.e. if the player chooses a new kit vie the KitsChooseInventory)
     */
    KIT_MANAGER_CLEAR_INVENTORY_ON_KIT_SET,
    /**
     * If a player buys a kit, he has access to it for the full server lifetime.
     * (The {@link eu.mcone.gameapi.api.player.GamePlayer#buyKit(Kit)} method will not remove coins for a second purchase of the same kit while the server does not restart.)
     */
    KIT_MANAGER_CHOOSE_KITS_FOR_SERVER_LIFETIME,
    /**
     * Enables auto buy kit function in Kits Inventory. This allows the player to enable automatic buying of the last kit that the player had before death on respawn.
     */
    KIT_MANAGER_ALLOW_AUTO_BUY_KIT,
    /**
     * As the players current kit gets saved on kit set, the last choosed kit can be set again after a rejoin to the server if its not save to store the player items elsewhere.
     */
    KIT_MANAGER_SET_CURRENT_KIT_ON_JOIN,

    //AchievementManager
    /**
     * Don't use this!
     * This loads the achievements from all gamemodes into memory (useful for lobby server to give an overview of all achievements)
     */
    ACHIEVEMENT_MANAGER_LOAD_ALL_ACHIEVEMENTS,

    //ReplayManager
    DOWNLOAD_REPLAY_WORLDS,
    USE_REPLAY_VIEW_MANAGER,

    //TeamManager
    /**
     * If a player dies, he wont get respawned as normally but will be put in spectator mode. This means that players can no longer play if they die.
     */
    TEAM_MANAGER_DISABLE_RESPAWN,
    /**
     * Normally the game stops automatically if there is only one team left.
     * Use the option to prevent this behaviour. You have to manually use {@link TeamManager#stopGameWithWinner(Team)} to stop the game.
     * You can calculate the winner team by the following methods: {@link TeamManager#calculateWinnerByGoals()}, {@link TeamManager#calculateWinnerByKills()}
     */
    TEAM_MANAGER_DISABLE_WIN_METHOD,

    //GameHistory
    GAME_HISTORY_HISTORY_MODE

}
