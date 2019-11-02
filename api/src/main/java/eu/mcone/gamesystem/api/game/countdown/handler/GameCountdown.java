/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.countdown.handler;

public interface GameCountdown {

    GameCountdownID getID();

    boolean isRunning();

    void setSeconds(int i);

    int getSeconds();

    int getStaticSeconds();

    int getRunTaskID();

    int getIdleTaskID();

    void run();

    void idle();

    void reset();

    void stop();

    void stopRunning();

    void stopIdling();

    void forceStop();

}
