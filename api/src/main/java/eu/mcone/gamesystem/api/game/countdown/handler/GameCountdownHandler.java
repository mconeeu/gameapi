/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.countdown.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameCountdownHandler {

    private Map<GameCountdownID, IGameCountdown> gameCountdowns;

    public GameCountdownHandler() {
        this.gameCountdowns = new HashMap<>();
    }

    public IGameCountdown getGameCountdown(GameCountdownID gameCountdownID) {
        return gameCountdowns.get(gameCountdownID);
    }

    public Collection<IGameCountdown> getGameCountdowns() {
        return gameCountdowns.values();
    }
}
