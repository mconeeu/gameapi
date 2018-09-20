package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.YAML_Config;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownHandler;
import eu.mcone.gamesystem.api.game.manager.map.MapManager;
import eu.mcone.gamesystem.api.game.manager.team.TeamManager;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class GameTemplate extends CorePlugin {

    @Getter
    private static GameTemplate instance;
    @Getter
    private YAML_Config gameSettings;

    @Getter
    private Map<UUID, GamePlayer> gamePlayers;
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
    private MapManager mapManager;
    @Getter
    @Setter
    private TeamManager teamManager;
    @Getter
    private GameCountdownHandler gameCountdownHandler;

    @Getter
    private int numberOfTeams;
    @Getter
    private int playerPreTeam;

    protected GameTemplate(String pluginName, ChatColor pluginColor, String prefixTranslation) {
        super(pluginName, pluginColor, prefixTranslation);

        instance = this;
        gamePlayers = new HashMap<>();
        playing = new ArrayList<>();
        teams = new HashMap<>();
        chats = new HashMap<>();
        spectators = new ArrayList<>();

        this.gameSettings = new YAML_Config(this, "GameSettings.yml");
        setGameSettings();

        gameCountdownHandler = new GameCountdownHandler();

        this.numberOfTeams = gameSettings.getConfig().getInt("teams");
        this.playerPreTeam = gameSettings.getConfig().getInt("playersPerTeam");

        Playing.Min_Players.setValue(gameSettings.getConfig().getInt("minPlayers"));
        Playing.Max_Players.setValue(gameSettings.getConfig().getInt("maxPlayers"));
    }

    private void setGameSettings() {
        this.gameSettings.getConfig().options()
                .header("Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved\n" +
                        "Current version: " + getDescription().getVersion());
        this.gameSettings.getConfig().options().copyHeader(true);
        this.gameSettings.getConfig().options().copyDefaults(true);
        this.gameSettings.getConfig().addDefault("lobbyWorld", "null");
        this.gameSettings.getConfig().addDefault("minPlayers", 2);
        this.gameSettings.getConfig().addDefault("maxPlayers", 2);
        this.gameSettings.getConfig().addDefault("teams", 4);
        this.gameSettings.getConfig().addDefault("playersPerTeam", 1);
        this.gameSettings.save();
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
}
