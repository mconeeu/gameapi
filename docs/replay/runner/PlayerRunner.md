PlayerRunner processes all packages only for the specified player.


> **Attention!** 
> Any changes you make to a PlayerRunner object and not via the PlayerRunnerManager object can also affect the behavior of the entire replay. 
> Changes to the PlayerRunner can cause the player to move out of sync with the other players.

### `getReplaySpeed()`
Returns the current speed of the runner.

### `setSpeed(ReplaySpeed speed)`
Sets the current speed of the runner.

| Speed | Enum      |
| :---  | :---      |
| 0.5   | _0_5X     |
| 0.75  | _0_75X    |
| 1.25  | _1_25X    |
| 1.50  | _1_5X     |
| 2.00  | _2X       |

### `skip(int ticks)`
Skips the specified ticks.