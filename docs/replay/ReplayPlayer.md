The ReplayPlayer object stores all information about the player being record.

### `getUUID()`
Returns the UUID of the Player.

### `getData()`
This method returns the [data](Data) object of the player.
This object gets also stored in the database.

### `getInventoryViewers()`
Returns a Map with all player that currently views the inventory from the Player.
_Is needed for playing the replay._

### `getNpc()`
Returns the Player as NPC.
_Is needed for playing the replay._

### `openInventory(Player player)`
OPens the Inventory from the ReplayPlayer for the give player.
_Is needed for playing the replay._

### `getStats()`
Returns the current stats of the Player as Stats object.

### `getHealth()`
Returns the current health of the Player as int.

### `getFood()`
Returns the current food level of the Player as int.
