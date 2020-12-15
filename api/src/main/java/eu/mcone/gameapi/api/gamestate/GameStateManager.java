/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.gamestate;

public interface GameStateManager {

    GameStateManager addGameState(GameState gameState);

    GameStateManager addGameStateFirst(GameState gameState);

    GameStateManager addGameStateBefore(GameState gameState, GameState addBefore);

    GameStateManager addGameStateAfter(GameState gameState, GameState addAfter);

    void startGame();

    GameState getRunning();

    GameState getPrevious();

    GameState getNextGameState();

    boolean setNextGameState(boolean force);

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
