package eu.mcone.gameapi.api.gamestate;

public interface GameStateManager {
    GameStateManager addGameState(GameState gameState);

    GameStateManager addGameStateFirst(GameState gameState);

    void startGame();

    GameState getNextGameState();

    boolean setGameState(GameState gameState, boolean force);

    boolean cancelTimeout();

    boolean startTimeout();

    boolean startTimeout(boolean force);

    boolean startTimeout(boolean force, long countdown);

    boolean cancelCountdown();

    boolean startCountdown();

    boolean startCountdown(boolean force);

    boolean startCountdown(boolean force, int countdown);
}
