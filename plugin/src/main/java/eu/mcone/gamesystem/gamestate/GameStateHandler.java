/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.gamestate;

import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.game.event.GameStateChangeEvenet;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class GameStateHandler implements eu.mcone.gamesystem.api.gamestate.GameStateHandler {

    @Getter
    private GameStateID currentStateID;
    @Getter
    private Map<GameStateID, Long> changeList;

    public GameStateHandler() {
        currentStateID = GameStateID.ERROR;
        changeList = new HashMap<>();
    }

    public void setGameState(GameStateID id) {
        int i = 0;
        for (GameStateID gameStateID : GameStateID.values()) {
            if (i <= GameStateID.values().length) {
                if (gameStateID.equals(id)) {
                    currentStateID = gameStateID;
                    changeList.put(id, System.currentTimeMillis() / 1000);
                    Bukkit.getPluginManager().callEvent(new GameStateChangeEvenet(id));
                    break;
                }
            } else {
                GameSystemAPI.getInstance().sendConsoleMessage("§cThe GameState with the ID `" + id + "` does not exist");
            }
        }
    }

    public boolean hasGameState(GameStateID type) {
        return currentStateID == type;
    }

}
