/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.countdown.handler;

import eu.mcone.gamesystem.api.game.gamestate.GameStateID;
import lombok.Getter;

public enum GameCountdownID {
    LOBBY_COUNTDOWN(1, GameStateID.LOBBY),
    SPAWN_COUNTDOWN(2, GameStateID.INGAME),
    RESTART_COUNTDOWN(3, GameStateID.END);

    @Getter
    private int ID;
    @Getter
    private GameStateID gameStateID;

    GameCountdownID(int ID, GameStateID gameStateID) {
        this.ID = ID;
        this.gameStateID = gameStateID;
    }
}
