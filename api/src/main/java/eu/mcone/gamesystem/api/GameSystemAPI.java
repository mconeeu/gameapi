/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.gamesystem.api.gamestate.GameStateHandler;
import eu.mcone.gamesystem.api.player.DamageLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public abstract class GameSystemAPI extends JavaPlugin {

    @Getter
    private static GameSystemAPI instance;

    /**
     * @param instance Sets the object 'instance' to a new GameSystem object.
     */
    protected void setInstance(final GameSystemAPI instance) {
        if (instance == null) {
            Bukkit.getConsoleSender().sendMessage("§cGameSystem instance cannot be set twice!");
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
