package eu.mcone.gameapi.api.gamestate;

public interface GameStateManager {
    GameStateManager addGameState(GameState gameState);

    GameStateManager addGameStateFirst(GameState gameState);

    void startGame();

    GameState getNextGameState();

    boolean setGameState(GameState gameState, boolean force);
}
