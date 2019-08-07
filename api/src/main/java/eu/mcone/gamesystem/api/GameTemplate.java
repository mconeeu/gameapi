/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gamesystem.api.config.GameSettingsConfig;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.achivements.AchievementManager;
import eu.mcone.gamesystem.api.game.gamestate.GameStateHandler;
import eu.mcone.gamesystem.api.game.manager.kit.KitManager;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.api.game.manager.team.TeamManager;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.api.lobby.cards.ItemCardManager;
import eu.mcone.gamesystem.api.lobby.manager.TrailManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class GameTemplate extends CorePlugin {

    @Getter
    private static GameTemplate instance;

    @Getter
    private List<GameSystemOptions> options;

    private Map<UUID, GamePlayer> gamePlayers;

    @Getter
    private List<Player> playing;
    @Getter
    private Map<UUID, Team> teams;
    @Getter
    private Map<Team, List<Player>> chats;
    @Getter
    private List<Player> spectators;

    @Setter
    private IMapManager mapManager;

    @Setter
    private TeamManager teamManager;

    @Setter
    private AchievementManager achievementManager;

    @Setter
    private TrailManager trailManager;

    @Setter
    private ItemCardManager itemCardManager;

    @Setter
    private KitManager kitManager;

    @Getter
    private GameStateHandler gameStateHandler;

    @Getter
    private Gamemode gamemode;
    @Getter
    private CoreJsonConfig<GameSettingsConfig> gameConfig;
    @Getter
    private GameSettingsConfig gameConfigAsClass;

    protected GameTemplate(String pluginName, Gamemode gamemode, ChatColor pluginColor, String prefixTranslation, GameSystemOptions... options) {
        super(pluginName, pluginColor, prefixTranslation);

        instance = this;

        this.gamemode = gamemode;
        this.options = Arrays.asList(options);

        gamePlayers = new HashMap<>();
        playing = new ArrayList<>();
        teams = new HashMap<>();
        chats = new HashMap<>();
        spectators = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        this.gameConfig = new CoreJsonConfig<>(this, GameSettingsConfig.class, "gameSettings.json");
        this.gameConfigAsClass = gameConfig.parseConfig();

        if (this.options.contains(GameSystemOptions.USE_GAME_STATE_HANDLER)
                || this.options.contains(GameSystemOptions.USE_TEAM_MANAGER)) {

            Playing.Min_Players.setValue(getGameConfigAsClass().getMinimalPlayers());
            Playing.Max_Players.setValue(getGameConfigAsClass().getMaximalPlayers());

            gameStateHandler = new GameStateHandler();
        }

        onGameEnable();
    }

    @Override
    public void onDisable() {
        gameConfig.save();

        if (trailManager != null) {
            trailManager.shutdown();
        }

        if (itemCardManager != null) {
            itemCardManager.saveItemCards();
        }

        for (GamePlayer gamePlayer : getGamePlayersAsList()) {
            gamePlayer.removeFromGame();
        }

        onGameDisable();
    }

    public abstract void onGameEnable();

    public abstract void onGameDisable();

    public void registerGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayers.put(gamePlayer.getCorePlayer().getUuid(), gamePlayer);
    }

    public void unregisterGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayers.remove(gamePlayer.getCorePlayer().getUuid());
    }

    public void unregisterGamePlayer(UUID uuid) {
        this.gamePlayers.remove(uuid);
    }

    /**
     * Returns a GamePlayer object with the UUID
     *
     * @param uuid Player UniqueId
     * @return GamePlayer object
     */
    public GamePlayer getGamePlayer(UUID uuid) {
        return gamePlayers.get(uuid);
    }

    /**
     * Returns a GamePlayer object with the Player
     *
     * @param player Player
     * @return GamePlayer object
     */
    public GamePlayer getGamePlayer(Player player) {
        return gamePlayers.get(player.getUniqueId());
    }

    /**
     * Returns a GamePlayer object with the Player name
     *
     * @param name Player name
     * @return GamePlayer object
     */
    public GamePlayer getGamePlayer(String name) {
        return gamePlayers.get(CoreSystem.getInstance().getPlayerUtils().fetchUuid(name));
    }

    /**
     * Returns the instance of the mapManager
     *
     * @return MapManager
     */
    public IMapManager getMapManager() {
        if (checkObj(mapManager)) {
            return mapManager;
        } else {
            return null;
        }
    }

    /**
     * Returns the instance of the TeamManager
     *
     * @return TeamManager
     */
    public TeamManager getTeamManager() {
        if (checkObj(teamManager)) {
            return teamManager;
        } else {
            return null;
        }
    }

    /**
     * Returns the instance of the AchievementManager
     *
     * @return AchievementManager
     */
    public AchievementManager getAchievementManager() {
        if (checkObj(achievementManager)) {
            return achievementManager;
        } else {
            return null;
        }
    }

    /**
     * Returns the instance of the TrailManager
     *
     * @return TrailManager
     */
    public TrailManager getTrailManager() {
        if (checkObj(trailManager)) {
            return trailManager;
        } else {
            return null;
        }
    }

    /**
     * Returns the instance of the ItemCardManager
     *
     * @return ItemCardManager
     */
    public ItemCardManager getItemCardManager() {
        if (checkObj(itemCardManager)) {
            return itemCardManager;
        } else {
            return null;
        }
    }

    /**
     * Returns the instance of the KitManager
     *
     * @return KitManager
     */
    public KitManager getKitManager() {
        if (checkObj(kitManager)) {
            return kitManager;
        } else {
            return null;
        }
    }

    /**
     * Checks if the specified object is null
     *
     * @param object
     * @return boolean
     */
    private boolean checkObj(Object object) {
        try {
            if (object != null) {
                return true;
            } else {
                throw new GameSystemException("Object is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns a Collection with all registered GamePlayer objects
     *
     * @return A Collection of all registered GamePlayers objects
     */
    public Collection<GamePlayer> getGamePlayersAsList() {
        return this.gamePlayers.values();
    }

    public enum GameSystemOptions {
        USE_GAME_STATE_HANDLER(),
        USE_TEAM_MANAGER(),
        USE_TEAM_STAGE(),
        USE_MAP_MANAGER(),
        USE_ACHIEVEMENTS(),
        USE_BACKPACK(),
        USE_ITEM_CARDS(),
        USE_KIT_MANAGER(),
        USE_ALL()
    }
}
