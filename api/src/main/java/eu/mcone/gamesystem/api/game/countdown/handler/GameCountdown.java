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

    void forceStop();

}
