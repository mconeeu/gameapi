package eu.mcone.gamesystem.api.game.countdown.handler;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class GameCountdownHandler {

    @Getter
    private Map<GameCountdownID, GameCountdown> gameCountdowns;

    public GameCountdownHandler() {
        this.gameCountdowns = new HashMap<>();
    }

    public void registerCountdown(GameCountdown... gameCountdowns) {
        for (GameCountdown gameCountdown : gameCountdowns) {
            this.gameCountdowns.put(gameCountdown.getID(), gameCountdown);
        }
    }

    public GameCountdown getGameCountdown(GameCountdownID gameCountdownID) {
        return gameCountdowns.get(gameCountdownID);
    }
}
