With the Achievement Manager you can easily create your own Achievements and give them to a player.
To use the Achievement manager you have to call `getAchievementManager()` in the **main class** of your plugin, this method instantiates a new instance of the Achievement manager.

### `registerAchievements(Achievement... achievements)`
Registers a list of Achievements. You must create for each Achievement one Achievement object.
The Achievement object contains the following informations about the Achievement:

| Variable      | Typ         | description                                                   |
| :---          | :---        | :---                                                          |
| name          | String      | The name of the Achievement (This name must be unique!)       |
| description   | String      | The description of the Achievement                            |
| rewardCoins   | Integer     | The reward the player becomes when he reaches the Achievement |

```java
//Create a Achievement
Achievement achievement = new Achievement(
        "Test", //Name
         "This is a test achievement!", //Description
        50 //Coins reward
);

GamePlugin.getInstance().getAchievementManager().registerAchievement(achievement);
```

### `reload()`
Caches all Achievements from the database if the `Option.ACHIEVEMENT_MANAGER_LOAD_ALL_ACHIEVEMENTS` Option is active.

### `getAchievement(String name)`
Returns an Achievement object from the local cache where the given name.
If it doesn't exist an Achievement with the given the method will throw a IllegalArgumentException.

### `getAchievement(Gamemode gamemode, String name)`
Returns an Achievement object from the local cache where the given gamemode and name.
If it doesn't exist an Achievement with the given the method will throw a IllegalArgumentException.

### `getAchievements(Gamemode gamemode)`
Returns a list of all Achievements from the local cache and database for the given gamemode.

### `setAchievement(GamePlayer player, Achievement achievement)`
Gives the given Player the specified achievement.
You can also give the player the Achievement via the GamePlayer object.
_Calls the AchievementGetEvent._

```java
GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer("DieserDominik");

//Option 1,
Achievement achievement = GamePlugin.getGamePlugin().getAchievementManager().getAchievement("Test");
GamePlugin.getGamePlugin().getAchievementManager().setAchievement(gamePlayer, achievement);

//Option 2
Achievement achievement = GamePlugin.getGamePlugin().getAchievementManager().getAchievement("Test");
gamePlayer.addAchievement(achievement);

//Option 3
gamePlayer.addAchievement("Test");
```

### `openAchievementInventory(Player p)`
Opens the achievement inventory for the given player.
The Achievement Inventory contains all Achievements that the player already have.

### `openAchievementInventory(Player p, Gamemode gamemode)`
Opens the achievement inventory for the given player and where the gamemode.
This Achievement Inventory contains all Achievements that the player already have for the specified gamemode.
