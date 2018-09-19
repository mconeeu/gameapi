/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

public interface GameStateHandler {

    /**
     * Set a new GameState
     * @param id Specifies the GameState type with the GameStateID.
     */
    void setGameState(GameStateID id);

    /**
     * Check if the GameState agrees with the current and returns true or false.
     *
     * @param type
     * @return true or false
     */
    boolean hasGameState(GameStateID type);

    /**
     * Returns the current GameState in the form of a GamesStateID
     * @return current GameStateID
     */
    GameStateID getCurrentStateID();

}
