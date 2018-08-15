/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.countdown;

public abstract class GameCountdown {

    /**
     * Start method of the GameCountdown
     */
    public abstract void start();

    /**
     * Stop method of the GameCountdown
     */
    public abstract void stop();

    /**
     * GameCountdown reset method (suitable for lobby, end countdowns)
     */
    public abstract void reset();
}
