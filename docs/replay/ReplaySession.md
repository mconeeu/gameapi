The ReplaySessions object is one of the most important objects of the ReplaySystem. 
With this object you can do everything with the replay what ever you want, you can play the replay, start the recording, get all information returned and much more.

### `getID()`
Returns a String with the unique sessionID.

### `getInfo()`
Returns a ServerInfo, this object contains all informations about the Server, startup, stopped, teams...

### `getRunnerManager()`
This method returns the runner manager object, with this object you can play your Replay.

### `getReplayRecorder()`
With this object you can get all currently recorder chunks of the replay.

### `getMessages()`
Returns a list of all sent messages in the Replay (this Map gets stored in the Database not in the Chunk Files).

### `recordSession()`
With this method you can start the recording manually.

### `saveSession()`
With this method you can save the currently running Replay. 
If it exists already a ReplayFile with the same SessionID this method will throw a ReplaySessionAlreadyExistsException.

### `addPlayer(Player player)`
Adds a player to the Replay, if the player is not added, he will not be included.
By default, the Player will automatically add to Replay if the ReplaySystem is active.
_Calls the PlayerJoinReplaySessionEvent._

### `removePlayer(Player player)`
Removes a player from the Replay, he will no longer get recorded.
_Calls the PlayerQuitReplaySessionEvent._

### `getReplayPlayer(UUID uuid)`
Returns a [ReplayPlayer](ReplayPlayer) object, this object will create if the player get added to the Replay.
The ReplayPlayer objects contains information about the Player, recorded packets, UUID...

### `getReplayPlayer(Player player)`
Returns a [ReplayPlayer](ReplayPlayer) object, this object will create if the player get added to the Replay.
The ReplayPlayer objects contains information about the Player, recorded packets, UUID...

### `getPlayers()`
Returns a Collection of all [ReplayPlayers](ReplayPlayer) in the Replay.

### `existstReplayPlayer(UUID uuid)`
Checks if a Player with the given uuid exists in the Replay.

### `existstReplayPlayer(Player player)`
Checks if a Player with the given `player.getUUID()` exists in the Replay.

### `openInformationInventory(Player player)`
Opens an Inventory for the given Player, this Inventory contains all Informations about the Replay.
**_This inventory will be moved to the Replay plugin in the future._**


