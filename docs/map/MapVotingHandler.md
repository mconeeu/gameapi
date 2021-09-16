With the Map Voting Handler the players have the possibility to vote for a certain map, the map with the most votes wins!

**How I can use the Map Voting Handler?**
```java
@Override
public void onGameEnable() {
    //First of all you have to instantiate a new map manager object.
    //You can do this easily by calling getMapManager() in the onGameEnabled() method of your main class.
    getMapManager();

    //Next, you must call the open voting inventory method.
    registerEvents(new Listener() {
        @EventHandler
        public void on(InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();

            if (e.getCurrentItem() != null
                    && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)
                    && e.getCurrentItem().getType().equals(Material.MAP)) {
                getMapManager().getMapVotingHandler().openVotingInventory(p, null);
            }
        }
    });

    //You don't have to do anything else because the system does the rest.
}
```
To stop the voting and to get a result you have to call the method `getMapManager().getVotingManager().getResult()` 
This method returns the map with the most votes and ends the voting.

### `openVotingInventory(Player p, CoreInventory coreInventory)`
Opens the voting Inventory for the give player.
The coreInventory is the inventory that is called when the player presses the back item in the Voting Inventory. 
If no coreInventory is given the back item will not be shown in the inventory.

### `getPopularityMap()`
Returns a map of all players who voted for a map.

### `calculateResult()`
Returns the result of the voting as GameAPIMap object.

