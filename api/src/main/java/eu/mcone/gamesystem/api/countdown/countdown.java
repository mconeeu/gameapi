/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */


package eu.mcone.gamesystem.api.countdown;

public abstract class countdown {

    /**
     * Start method of the countdown
     */
    public abstract void start();

    /**
     * Stop method of the countdown
     */
    public abstract void stop();

    /**
     * countdown reset method (suitable for lobby, end countdowns)
     */
    public abstract void reset();
}
