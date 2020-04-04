package eu.mcone.gameapi.api.replay.runner;

import lombok.Getter;

@Getter
public enum ReplaySpeed {
    _0_5X(false, 2, 0.5),
    _0_75X(false, 4, 0.75),

    _1_25X(true, 3, 1.25),
    _1_5X(true, 2, 1.5),
    _2X(true, 0, 2.0);

    private boolean add;
    private int wait;
    private double speed;

    ReplaySpeed(boolean add, int wait, double speed) {
        this.add = add;
        this.wait = wait;
        this.speed = speed;
    }

    public static ReplaySpeed getWhereSpeed(double speed) {
        for (ReplaySpeed replaySpeed : values()) {
            if (replaySpeed.speed == speed) {
                return replaySpeed;
            }
        }

        return null;
    }
}
