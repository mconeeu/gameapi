/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.gamestate;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.countdown.LobbyCountdown;
import eu.mcone.gamesystem.api.game.countdown.RestartCountdown;
import eu.mcone.gamesystem.api.game.countdown.SpawnCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.event.GameStateChangeEvent;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class GameStateHandler {

    @Getter
    private GameStateID currentStateID;
    @Getter
    private Map<GameStateID, Long> changeList;
    @Getter
    private Map<GameStateID, GameCountdown> gameCountdowns;

    public GameStateHandler() {
        currentStateID = GameStateID.ERROR;
        changeList = new HashMap<>();
        gameCountdowns = new HashMap<>();
    }

    public void changeGameState(GameStateID id) {
        if (!currentStateID.equals(id)) {
            GameTemplate.getInstance().sendConsoleMessage("Change CurrentState `" + currentStateID + "` to `" + id + "`");
            currentStateID = id;
            changeList.put(id, System.currentTimeMillis() / 1000);
            Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(id));
        } else {
            GameTemplate.getInstance().sendConsoleMessage("The current state is already set to " + id.toString());
        }
    }

    public void registerGameCountdown(GameCountdownID gameCountdownID, int seconds) {
        switch (gameCountdownID) {
            case LOBBY_COUNTDOWN:
                gameCountdowns.put(gameCountdownID.getGameStateID(), new LobbyCountdown(seconds));
                break;
            case SPAWN_COUNTDOWN:
                gameCountdowns.put(gameCountdownID.getGameStateID(), new SpawnCountdown(seconds));
                break;
            case RESTART_COUNTDOWN:
                gameCountdowns.put(gameCountdownID.getGameStateID(), new RestartCountdown(seconds));
                break;
        }
    }

    public GameCountdown getGameCountdown(GameCountdownID gameCountdownID) {
        return getGameCountdown(gameCountdownID.getGameStateID());
    }

    public GameCountdown getGameCountdown(GameStateID gameStateID) {
        if (gameCountdowns.containsKey(gameStateID)) {
            System.out.println(gameCountdowns.get(gameStateID).getID());
            return gameCountdowns.get(gameStateID);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cCannot find GameCountdown for StateID " + gameStateID);
            return null;
        }
    }

    public boolean hasGameCountdown(GameStateID gameStateID) {
        return gameCountdowns.containsKey(gameStateID);
    }

    public GameCountdown getCurrentGameCountdown() {
        if (gameCountdowns.containsKey(currentStateID)) {
            return gameCountdowns.get(currentStateID);
        }

        return null;
    }

    public boolean hasGameState(GameStateID id) {
        return currentStateID.equals(id);
    }
}
