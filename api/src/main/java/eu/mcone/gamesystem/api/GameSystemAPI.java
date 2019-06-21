/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.player.IDamageLogger;
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
     * @return Returns the DamageLogger
     */
    public abstract IDamageLogger getDamageLogger();

    /**
     * Create a new GameCountdown and returns the GameCountdown interface
     * @param countdownID
     * @param seconds
     * @return GameCountdown interface
     */
    public abstract IGameCountdown registerCountdown(GameCountdownID countdownID, int seconds);
}
