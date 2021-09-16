This is a short Introduction in the ReplaySystem. The ReplaySystem is a very big a complex System.
The basic principle of the ReplaySystem is to get the sent packets of the player and write them into a file.
These can then be played back later on an [NPC](https://gitlab.onegaming.group/systems/coresystem/-/wikis/bukkit/npc/home). 
Currently, the following packages are stored in the replay file:

| Packet                | Typ           |
| :---                  | :---          |
| BLOCK_ACTION          | CLIENT        |
| BLOCK_BREAK           | CLIENT        |
| BLOCK_PLACE           | CLIENT        |
| TNT_PRIME             | CLIENT        |
| INVENTORY_CHANGE_ITEM | CLIENT        |
| INVENTORY_CHANGE      | CLIENT        |
| ARMOR_CHANGE          | CLIENT        |
| CHANGE_STATE          | CLIENT        |
| DEATH                 | CLIENT/SERVER |
| DESTROY               | CLIENT/SERVER | 
| DROP_ITEM             | CLIENT        |
| LAUNCH_PROJECTILE     | CLIENT        |
| PICK_ITEM_UP          | CLIENT        |
| PLAY_NAMED_SOUND      | CLIENT/SERVER |
| RESPAWN               | CLIENT/SEVER  |
| SEND_CHAT_MESSAGE     | CLIENT/SERVER |
| SHOOT_ARROW           | CLIENT        |
| SPAWN                 | SERVER        |
| STATS_CHANGE          | CLIENT        |
| BUTTON_INTERACT       | CLIENT        |
| CLICK                 | CLIENT        |
| DAMAGE                | CLIENT        |
| MOVE                  | CLIENT        |
| OPEN_DOOR             | CLIENT        |
| TNT_EXPLODE           | SERVER        |
| BROADCAST_MESSAGE     | SERVER        |

*In the future there will be the possibility to add own packages in the form of codecs.*   

**How is the ReplaySystem structured?**    
When you start a recording, a so-called [ReplaySession](ReplaySession) is created in the background which contains all information about the replay. 
(For each ReplaySession the System generates a unique [SessionID](ReplaySession)!) 
Each replay can be seen as a separate replay session. In this ReplaySession the recorded packages of the players are saved. 
These are divided into so-called [ReplayChunks](chunk/ReplayChunk), a chunk is usually **30 seconds** long. 
After these 30 seconds a new chunk is created, and the old chunk is stored in the meantime.
When the replay is finished all ReplayChunks are serialized and put into single files marked with **CHUNK**:**ID** which are then all put into one ZIP file which is then uploaded to the ReplayServer.
Also, the world that appears in the replay will be uploaded to the database if it does not already exist.

**How can I use the ReplaySystem?** 
You can easily use the ReplaySystem by calling the methods:
 * `getGameStateHandler()` 
    The GameStateHandler is needed to record and save the replay automatically, important here are the classes InGameState and EndState from the GameState, Only these two classes have the possibility to start or stop the replay automatically. 
    If you want to create your own InGameState or EndState class you can also simply extend the two classes mentioned above.
 * `getReplaySessionManager()`
    The ReplaySessionManager generates the unique SessionID for the ReplaySession.
 * `getReplaySession()`
    The getReplaySession method generates the actual Replay object.
 * `getPlayerManager()`
    The PlayerManager is needed to automatically register the joining players in the replay.
 * `getTeamManager()`
    The TeamManager is not necessarily required, but if it is activated, the ReplaySystem notices this and saves the respective team for the player.
The prerequisite for this is that you extend the GamePlugin in the plugin. 

```java
@Getter
public class Test extends GamePlugin {

    public Test() {
        super("test", ChatColor.RED, "test.prefix");
    }

    @Getter
    public static Test instance;

    @Override
    public void onGameEnable() {
        instance = this;
        getGameStateManager().addGameState(new LobbyState()).addGameState(new IngameState()).addGameState(new EndState()).startGame();
    
        //Without these two methods no Repaly will be created!
        getSessionManager();
        getReplaySession();
        
        getPlayerManager();

        //Optional!
        getTeamManager();

        sendConsoleMessage("§4TEST PLUGIN STARTED");
    }

    @Override
    public void onGameDisable() {
    }
}
```

#### List of all Classes:
* [ReplaySession](ReplaySession)
* [ReplayPlayer](ReplayPlayer)
* [Data](Data)
* [PlayerRunner](runner/PlayerRunner)
* [PlayerRunnerHandler](runner/PlayerRunnerManager)
* [ReplaySessionHandler](ReplaySessionManager)
* [ReplayChunk](chunk/ReplayChunk)
* [ReplayChunkHandler](chunk/ReplayChunkHandler)
