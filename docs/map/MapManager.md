With the MapManager you can manage all maps you need for the game mode.
All maps you need for the game must be stored in the maps.json. 
You can find the maps.json under. `./plugins/{pluginName}/maps.json`

Add a map to the file:
```json
{
    "maps": [
        {
            "name": "TEST",
            "lore": [ 
                "TEST-Map",
                "test"
            ],
            "item": "BARRIER"
        }
      ],
      "lastRotation": 0
}
```

**GameAPIMap object:**

| Variable      | Typ           | description                                           |
| :---          | :---          | :---                                                  |
| name          | String        | The name of the map, must be unique                   |
| lore          | List<String>  | The description of the map showen in the Inventory    |
| item          | Material      | The item of the map                                   |

### `addMap(CoreWorld world, List<String> lore, Material item)`
Adds a map to the mapManager list and maps.json file.

### `addMap(CoreWorld world, Material item)`
Adds a map to the mapManager list and maps.json file.

### `getMap(String name)`
Returns a **GameAPIMap** object where the specified name.

### `getMapVotingHandler()`
Instantiated a new [map voting handler](MapVotingHandler) object. 
With the Map Voting handler players can vote for a certain map, and the map with the most votes will be selected.
_This method can throw a **IllegalStateException**!_

### `getMapRotationHandler()`
Instantiated a new [map rotation handler](MapRotationHandler) object.
The map rotation handler changes the map automatically after a set time. 
To use the map rotation handler, at least **2 maps** must be available.
_This method can throw a **IllegalStateException**!_