With the ReplaySessionManager you have the possibility to manage all ReplaySessions and to change them if necessary.

### `isCache()`
This method returns whether all ReplaySessions from the database are stored locally.
If you want to cache all replays from the database you can set the option  `Option.REPLAY_SESSION_MANAGER_LOAD_ALL_REPLAYS` in your main class. 
But ATTENTION! with many replays in the database the memory gets full very fast so this option should only be used in exceptional cases.

### `getWorldDownloader()`
This method returns the WorldDonwload, with the world downloader you can upload and download a wrodl from to the Database.

### `saveSession(ReplaySession session)`
With this method you can save the given session in the database. 
If it exists already an ReplaySession with the same SessionID the method will produce an **ReplaySessionAlreadyExistsException**.

### `deleteSession(String sessionID)`
With this method you can delete the given sessionID from the database.
If it was successfully the method will return **true**.

### `getLiveSession(String sessionID)`
Returns a LiveSession object for the give sessionID from the **database**.
If it doesn't exist a ReplaySession for the given sessionID the method will produce an **ReplaySessionNotFoundException**.

### `getSession(String sessionID)`
Returns a LiveSession object for the give sessionID from the **local Cache**.
If it doesn't exist a ReplaySession for the given sessionID the method will produce an **ReplaySessionNotFoundException**.

### `getSessionsForPlayer(UUID uuid)`
Returns a list of all ReplaySession for the give Player from the **local Cache**.

### `existsSession(String sessionID)`
Checks if a ReplaySession for the given sessionID exists in the **database** or **local cache**. 

### `getLiveSessions(int startIndex, int values)`
Returns a Collection with a defined amount (values) of ReplaySessions in the database for the specified startIndex.

### `getLiveSessions()`
Returns a Collection of all ReplaySessions in the database.

### `getSessions()`
Returns a Collection of all local cache ReplaySessions.
