package eu.mcone.gameapi.api.replay.runner;

import lombok.Getter;

@Getter
public enum ReplaySpeed {
    _0_5X(false, 2, 150), //3 Bukkit ticks
    _0_75X(false, 4, 100), //2 Bukkit ticks
    _1X(true, 3, 50), //1 Bukkit tick

    _1_25X(true, 3, 35),
    _1_5X(true, 2, 25),
    _2X(true, 0, 15);

    private boolean add;
    private int wait;
    private int speed;

    /**
     *
     * @param add
     * @param wait
     * @param speed Milliseconds
     */
    ReplaySpeed(boolean add, int wait, int speed) {
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
