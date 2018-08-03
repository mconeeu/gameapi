/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

public interface GameState {

    /**
     * Part in which the GameCountdown is registered
     */
    void init();

    /**
     * Part in which the GameCountdown can be stopped
     */
    void end();

    /**
     * Part in which the GameCountdown in GameStateHandler can be registered
     */
    void register();

}
