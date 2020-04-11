package eu.mcone.gameapi.api.gamestate;

import eu.mcone.gameapi.api.Option;

import java.util.List;

public interface GameStateManager {

    List<Option> getOptions();

    GameStateManager addGameState(GameState gameState);

    GameStateManager addGameStateFirst(GameState gameState);

    void startGame();

    GameState getRunning();

    GameState getPrevious();

    GameState getNextGameState();

    boolean setGameState(GameState gameState, boolean force);

    boolean cancelTimeout();

    boolean isTimeoutRunning();

    boolean startTimeout();

    boolean startTimeout(boolean force);

    boolean startTimeout(boolean force, long countdown);

    boolean updateTimeoutCounter(long second);

    boolean isCountdownRunning();

    boolean cancelCountdown();

    boolean startCountdown();

    boolean startCountdown(boolean force);

    boolean startCountdown(boolean force, int countdown);

    int getCountdownCounter();

    long getTimeoutCounter();

    boolean updateCountdownCounter(int second);
}
