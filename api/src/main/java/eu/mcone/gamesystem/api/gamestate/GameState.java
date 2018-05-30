/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

public interface GameState {

    /**
     * Part in which the countdown is registered
     */
    void init();

    /**
     * Part in which the countdown can be stopped
     */
    void end();

    /**
     * Part in which the countdown in GameStateHandler can be registered
     */
    void register();

}
