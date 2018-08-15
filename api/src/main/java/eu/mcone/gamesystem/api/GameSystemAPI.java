/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gamesystem.api.gamestate.GameStateHandler;
import eu.mcone.gamesystem.api.player.DamageLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

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

}
