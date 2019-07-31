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
import eu.mcone.gamesystem.api.game.achivements.IAchievementManager;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.api.game.manager.team.ITeamManager;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;
import eu.mcone.gamesystem.api.game.gamestate.GameStateHandler;
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

    @Getter
    private Map<UUID, IGamePlayer> gamePlayers;
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
    private ITeamManager teamManager;

    @Setter
    private IAchievementManager achievementManager;

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
        onGameDisable();
    }

    public abstract void onGameEnable();

    public abstract void onGameDisable();

    /**
     * Returns a GamePlayer object with the UUID
     *
     * @param uuid Player UniqueId
     * @return GamePlayer object
     */
    public IGamePlayer getGamePlayer(UUID uuid) {
        return gamePlayers.get(uuid);
    }

    /**
     * Returns a GamePlayer object with the Player
     *
     * @param player Player
     * @return GamePlayer object
     */
    public IGamePlayer getGamePlayer(Player player) {
        return gamePlayers.get(player.getUniqueId());
    }

    /**
     * Returns a GamePlayer object with the Player name
     *
     * @param name Player name
     * @return GamePlayer object
     */
    public IGamePlayer getGamePlayer(String name) {
        return gamePlayers.get(CoreSystem.getInstance().getPlayerUtils().fetchUuid(name));
    }

    /**
     * Returns the instance of the mapManager
     *
     * @return IMapManager
     */
    public IMapManager getMapManager() {
        try {
            if (this.mapManager != null) {
                return mapManager;
            } else {
                throw new GameSystemException("MapManager is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the instance of the TeamManager
     *
     * @return ITeamManager
     */
    public ITeamManager getTeamManager() {
        try {
            if (this.teamManager != null) {
                return teamManager;
            } else {
                throw new GameSystemException("TeamManager is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the instance of the AchievementManager
     *
     * @return IAchievementManager
     */
    public IAchievementManager getAchievementManager() {
        try {
            if (this.achievementManager != null) {
                return achievementManager;
            } else {
                throw new GameSystemException("AchievementManager is not initialized!");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns a Collection with all registered GamePlayer objects
     *
     * @return A Collection of all registered GamePlayers objects
     */
    public Collection<IGamePlayer> getGamePlayersAsList() {
        return this.gamePlayers.values();
    }

    public enum GameSystemOptions {
        USE_GAME_STATE_HANDLER(),
        USE_TEAM_MANAGER(),
        USE_TEAM_STAGE(),
        USE_MAP_MANAGER(),
        USE_ACHIEVEMENTS()
    }
}
