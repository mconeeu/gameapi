/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.ConfigParser;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gamesystem.api.config.GameSettingsConfig;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.achivements.IAchievementManager;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownHandler;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.api.game.manager.team.TeamManager;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;
import eu.mcone.gamesystem.api.gamestate.GameStateHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class GameTemplate extends CorePlugin {

    @Getter
    private static GameTemplate instance;

    @Getter
    private GameSettingsConfig gameSettingsConfig;

    @Getter
    private List<Options> options;

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

    @Getter
    @Setter
    private IMapManager mapManager;
    @Getter
    @Setter
    private TeamManager teamManager;
    @Setter
    @Getter
    private IAchievementManager achievementManager;
    @Getter
    private GameStateHandler gameStateHandler;
    @Getter
    private GameCountdownHandler gameCountdownHandler;


    @Getter
    private Gamemode gamemode;
    @Getter
    private ConfigParser configParser;

    protected GameTemplate(String pluginName, Gamemode gamemode, ChatColor pluginColor, String prefixTranslation, Options... options) {
        super(pluginName, pluginColor, prefixTranslation);

        instance = this;

        this.gamemode = gamemode;
        this.options = Arrays.asList(options);
        this.configParser = new ConfigParser();
        gamePlayers = new HashMap<>();
        playing = new ArrayList<>();
        teams = new HashMap<>();
        chats = new HashMap<>();
        spectators = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        this.gameSettingsConfig = configParser.loadAndParseConfig("./plugins/" + GameTemplate.getInstance().getPluginName(), "gameSettings.json", GameSettingsConfig.class);

        if (this.options.contains(Options.USE_GAME_STATE_HANDLER)
                || this.options.contains(Options.USE_TEAM_MANAGER)) {

            Playing.Min_Players.setValue(gameSettingsConfig.getMinimalPlayers());
            Playing.Max_Players.setValue(gameSettingsConfig.getMaximalPlayers());

            gameStateHandler = new GameStateHandler();

            gameCountdownHandler = new GameCountdownHandler();
        }

        onGameEnable();
    }

    @Override
    public void onDisable() {
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
     * Returns a Collection with all registered GamePlayer objects
     *
     * @return A Collection of all registered GamePlayers objects
     */
    public Collection<IGamePlayer> getGamePlayersAsList() {
        return this.gamePlayers.values();
    }

    public enum Options {
        USE_GAME_STATE_HANDLER(),
        USE_TEAM_MANAGER(),
        USE_TEAM_STAGE(),
        USE_MAP_MANAGER(),
        USE_ACHIEVEMENTS()
    }
}
