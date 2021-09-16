The BackpackManager contains all items from the game modes. 
Find out more about the Backpack on [Team Wiki](https://wiki.onegaming.group/gamemanager#rucksack-items).
With the BackpackManager api you have the possibility to activate or deactivate certain categories of the backpack. 

**How I can use the Backpack Manager?**
```java
@Override
public void onGameEnable() {
    //Initiate a new BackpackManager object
    getBackpackManager();

    //First of all you have to register a backpack category, by default some categories are already registered.
    //For example the following categories are by default registered, HAT, GADGET, TRAIL...
    GamePlugin.getGamePlugin().getBackpackManager().registerCategory(
            new Category(
                    "TEST-Category", //The name of the category.
                    "TEST-Description", //The description of the category.
                    false, //Is the item sellable? If it is sellable you can trade this item with other players.
                    true, //Shown in Backpack?
                    1, //Sets the position of the category in the Backpack.
                    Gamemode.UNDEFINED, //Sets the gamemode for the category, only for this gamemode the category is available.
                    new ItemBuilder(Material.FEATHER, 1).create() //Sets the display item of the category.
            ),
            new HashSet<BackpackItem>() {{
                add(new BackpackItem(
                        1, //The id for the item must be unique for the category.
                        "Test-Item-1", //The name of the item
                        Level.UNUSUAL, //The level of the item, also shown in the chest opening.
                        new ItemBuilder(Material.BARRIER, 1).create(), //The item
                        false, //Is the item in the chest opening winnable?
                        false, //Is the item buyable by the lobby traider?
                        false, //Is the item sellable?
                        50, //Price for the Item
                        40 //Sell price from the Item
                ));

                add(new BackpackItem(
                        2,
                        "Test-Item-2",
                        Level.UNUSUAL,
                        new ItemBuilder(Material.DIRT, 1).create(),
                        true,
                        true,
                        true,
                        20,
                        10
                ));
            }},
            new BackpackInventoryListener() {
                @Override
                public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player player) {
                    super.onBackpackInventoryClick(item, gamePlayer, player);
                    //Gets called when the player clicks something in the backpack Inventory.
                }

                @Override
                public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gamePlayer, Player player) {
                    super.setBackpackItems(inv, category, categoryItems, gamePlayer, player);
                    //Gets called when the player clicks on one item.
                }
            }
    );
}
```

### `getGameOptions()`
Returns a list of all options that are set for the backpack manager.

### `getDisabledItems()`
Returns a hashset of all disabled default items.

### `isUseRankBoots()`
Rank boots are activated?

### `getItemSlot()`
Returns the slot where the items from the backpack manager get set, by default the item slot is 3.

### `setItemSlot(int slot)`
Sets the item slot.

### `getFallbackSlot()`
Returns the slot where the gadgets from the backpack manager get set, by default the item slot is 2.

### `setFallbackSlot(int slot)`
Sets the gadgets slot.

### `setUseRankBoots(boolean use)`
Sets if the boots get set for the player when he joins.

### `disableItem(DefaultItem item)`
Disable the give default item, this item gets not longer shown in the backpack manager.

### `disableItem(String item)`
Disable the default item where the given item name, this item gets not longer shown in the backpack manager.

### `registerCategory(Category category, Set<BackpackItem> items, BackpackInventoryListener listener)`
Registers a new backpack category. Can throw a **IllegalArgumentException**.

### `registerCategory(Category category, Set<BackpackItem> items)`
Registers a new backpack category. Can throw a **IllegalArgumentException**.

### `loadAdditionalCategories(String... names)`
Loads all given additional categories.

### `reload()`
Reloads the local cache.

### `getBackpackItem(String category, int id)`
Gets the backpack item where the given category and id.

### `openBackpackInventory(String category, Player player)`
Opens the backpack inventory where the category for the given player.

### `openBackpackSellInventory(String name, Player p)`
Opens the backpack inventory where the name for the given player.

### `categoryExists(String category)`
Checks if a category with the given name exists.

### `itemExists(BackpackItem item)`
Checks if the given item exists.
       

