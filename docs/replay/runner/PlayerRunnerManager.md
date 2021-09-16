The ReplayRunnerManager is responsible for the synchronous playback of all recorded packets.  
Each ReplayPlayer has a ReplayPlayer object in which the packages are processed one after the other.

### `getRunnedID()`
Returns the runner id as String

### `isForward()`
Returns if the replay goes forward.  

### `isBackward()`
Returns if the replay goes backward.

### `isShowProgress()`
Returns if the progress is shown in the hot bar.

### `setShowProgress(boolean progress)`
Sets whether the progress should be displayed in the hot bar.

### `getReplaySpeed()`
Returns the current speed of the replay.

### `setSpeed(ReplaySpeed speed)`
Sets the current speed of the replay.

| Speed | Enum      |
| :---  | :---      |
| 0.5   | _0_5X     |
| 0.75  | _0_75X    |
| 1.25  | _1_25X    |
| 1.50  | _1_5X     |
| 2.00  | _2X       |

### `play()`
Continues playing the replay.
_Calls the ReplayStartEvent._

### `isPlaying()`
Returns the replay plays.

### `stopPlaying()`
This pauses the Replay.
_Calls the ReplayStopPlayingEvent._

### `startPlaying()`
This starts the currently MotionCapture.
_Calls the ReplayStartPlayingEvent._

### `backward()`
Plays the replay backward.

### `backward()`
Plays the replay forward.

### `stop()`
Stops the replay from playing.
_Calls the ReplayStopEvent._

### `addWatcher(Player player)`
Adds a watcher to the Replay, only watchers can see the movements of the currently Playing Replay.
_Calls the WatcherJoinReplayEvent._
 
### `removeWatcher(Player player)`
Removes the given Player from the watcher list.
_Calls the WatcherQuitReplayEvent._

### `getWatchers()`
Returns a list of all currently watching Players.

### `getCurrentTick()`
Returns the current Tick of the Replay as AtomicInteger.

### `skip(int ticks)`
Skips the specified ticks.

### `getSingleRunners()`
Returns a collection of all PlayerRunner.

### `createSingleRunner(ReplayPlayer replayPlayer)`
This creates a PlayerRunner for one ReplayPlayer. 
This can be used to play a replay for only one player.

### `openSpectatorInventory(Player player)`
This opens for the given player a spectator inventory.