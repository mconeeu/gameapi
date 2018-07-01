/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.gamestate;

import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.gamestate.GameState;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class GameStateHandler implements eu.mcone.gamesystem.api.gamestate.GameStateHandler {

    private static GameState currentState;
    private static GameStateID currentStateID;
    private static HashMap<GameStateID, GameState> states = new HashMap<>();

    public GameStateHandler() {}

    public void registerGameStateClass(GameState gameState, GameStateID type) {
        if(states.containsKey(type)) {
            states.remove(type);
            states.put(type, gameState);
        } else {
            states.put(type, gameState);
        }
    }

    public void setGameState(GameStateID id) {
        if (currentState != null) currentState.end();
        if(states.size() > id.getValue() || states.size() == id.getValue()) {
            currentState = states.get(id);
            currentStateID = id;
            currentState.init();
        } else {
            GameSystem.getInstance().sendConsoleMessage("§cGameState: " + id + " not exists...");
        }
    }

    public boolean hasGameState(GameStateID type) {
        return currentStateID == type;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public GameStateID getCurrentStateID() {
        return currentStateID;
    }

}
