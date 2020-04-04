package eu.mcone.gameapi.api.replay.runner;

public interface PlayerRunner {

    ReplaySpeed getReplaySpeed();

    void setReplaySpeed(ReplaySpeed speed);

    void skip(int ticks);
}
