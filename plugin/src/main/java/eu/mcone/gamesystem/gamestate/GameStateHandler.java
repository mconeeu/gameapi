/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.gamestate;

import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.game.event.GameStateChangeEvent;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameStateHandler implements eu.mcone.gamesystem.api.gamestate.GameStateHandler {

    private Logger log;
    @Getter
    private GameStateID currentStateID;
    @Getter
    private Map<GameStateID, Long> changeList;

    public GameStateHandler() {
        log = GameSystemAPI.getInstance().getLogger();
        currentStateID = GameStateID.ERROR;
        changeList = new HashMap<>();
    }

    public void setGameState(GameStateID id) {
        int i = 0;
        for (GameStateID gameStateID : GameStateID.values()) {
            if (i <= GameStateID.values().length) {
                if (gameStateID.equals(id)) {
                    log.info("Change CurrentState `" + currentStateID + "` to `" + id + "`");
                    currentStateID = id;
                    changeList.put(id, System.currentTimeMillis() / 1000);
                    Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(id));
                    break;
                }
            } else {
                log.log(Level.SEVERE, "The GameState withe the id `" + id + "` does not exists");
            }
        }
    }

    public boolean hasGameState(GameStateID id) {
        return currentStateID == id;
    }
}
