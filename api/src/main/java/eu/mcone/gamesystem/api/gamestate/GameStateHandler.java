/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.game.event.GameStateChangeEvent;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GameStateHandler {

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
        if (!currentStateID.equals(id)) {
            log.info("Change CurrentState `" + currentStateID + "` to `" + id + "`");
            currentStateID = id;
            changeList.put(id, System.currentTimeMillis() / 1000);
            Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(id));
        } else {
            log.info("The current state is already set to " + id.toString());
        }
    }

    public boolean hasGameState(GameStateID id) {
        return currentStateID.equals(id);
    }
}
