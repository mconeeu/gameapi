/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.gamesystem.api.gamestate.GameStateHandler;
import eu.mcone.gamesystem.api.player.DamageLogger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class GameSystemAPI extends JavaPlugin {

    /* Last update: 19.05.2018 / 21:20 */

    private static String MainPrefix = "§8[§8GameAPI§8] ";
    private static GameSystemAPI instance;
    private static DamageLogger damageLogger;
    private static GameStateHandler gameStateHandler;

    private static HashMap<Integer, GameStateHandler> states;

    /**
     * Classes initialization constructor
     */
    public GameSystemAPI() {
        states = new HashMap<>();
    }

    /**
     * @param instance Sets the object 'instance' to a new GameSystem object.
     */
    protected void setInstance(final GameSystemAPI instance) {
        if (instance == null) {
            Bukkit.getConsoleSender().sendMessage(MainPrefix + "§cGameSystem instance cannot be set twice!");
        } else {
            GameSystemAPI.instance = instance;
        }
    }

    /**
     * @param damageLogger Sets the variable 'damageLogger' to a new DamageLogger object from the class GameSystem.
     */
    protected void setDamageLogger(final DamageLogger damageLogger) {
        GameSystemAPI.damageLogger = damageLogger;
    }

    /**
     * @param gameStateHandler Sets the variable 'gameStateHandler' to a new GameStateHandler object from the class GameSystem.
     */
    protected static void setGameStateHandler(GameStateHandler gameStateHandler) {
        GameSystemAPI.gameStateHandler = gameStateHandler;
    }

    /**
     * Adds a new GameStateHandler object to the states HashMap
     */
    public static void createGameStateHandler() {
        if(states.containsKey(1)) {
            Bukkit.getConsoleSender().sendMessage(MainPrefix + "§cThere is already a GameStateHandler with the ID...");
        } else {
            GameSystemAPI.states.put(1, GameSystemAPI.gameStateHandler);
        }
    }


    /**
     * The Default GameStateHandler return ID is 1
     */
    public static GameStateHandler getGameStateHandler(int id) {
        if (states.get(id) != null) {
            return states.get(id);
        } else {
            Bukkit.getConsoleSender().sendMessage(MainPrefix + "§cThere is no gamestate handler with the ID '" + id + "'");
            return null;
        }
    }

    /**
     * @return Returns the DamageLogger
     */
    public static DamageLogger getDamageLogger() {
        return GameSystemAPI.damageLogger;
    }


    /**
     * @return Returns the GameSystem instance
     */
    public static GameSystemAPI getInstance() {
        return GameSystemAPI.instance;
    }

}
