/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.manager.map.MapManager;
import eu.mcone.gamesystem.api.game.manager.team.TeamManager;
import eu.mcone.gamesystem.api.gamestate.GameStateHandler;
import eu.mcone.gamesystem.api.player.DamageLogger;
import lombok.Getter;
import org.bukkit.ChatColor;

public abstract class GameSystemAPI extends CorePlugin {

    public GameSystemAPI() {
        super("GameSystem", ChatColor.DARK_GRAY, "system.prefix.server");
    }

    @Getter
    private static GameSystemAPI instance;

    protected void setInstance(final GameSystemAPI instance) {
        if (instance == null) {
            System.err.println("LobbyPlugin instance cannot be set twice!");
        } else {
            GameSystemAPI.instance = instance;
        }
    }

    /**
     * The Default GameStateHandler return ID is 1
     */
    public abstract GameStateHandler getGameStateHandler();

    /**
     * @return Returns the DamageLogger
     */
    public abstract DamageLogger getDamageLogger();

    /**
     * Create a new MapManager object
     * @param instance CorePlugin
     * @return new MapManager object
     */
    public abstract MapManager createMapManager(CorePlugin instance);

    /**
     * Create a new TeamManager object
     * @return new TeamManager object
     */
    public abstract TeamManager createTeamManager();

    /**
     * Create a new GameCountdown and returns the GameCountdown interface
     * @param countdownID
     * @param seconds
     * @return GameCountdown interface
     */
    public abstract GameCountdown registerCountdown(GameCountdownID countdownID, int seconds);
}
