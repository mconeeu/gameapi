# GameSystem API
*The whole Javadoc of the GameSystem API can be found [here](http://systeme.gitlab.mcone.eu/gamesystem/).*

This Project contains all Game relevant Systems like handling or creating GameSates, Team management, Map management...
All core related APIs can be found in the [CoreSystem-api project](https://gitlab.onegaming.group/systems/coresystem/-/wikis/home)

Include in your project with maven:
```xml
<dependency> <!-- MCONE-GameSystem API -->
    <groupId>eu.mcone.gameapi</groupId>
    <artifactId>api</artifactId>
    <version>4.1.2-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```
Dont forget the MC ONE repository:
```xml
<repository>
    <id>onegaming-gitlab-systems</id>
    <url>https://gitlab.onegaming.group/api/v4/groups/systems/-/packages/maven</url>
</repository>
```

This is an overview of all Systems and functions containing he GameSystem-API.

### [TeamManager](./team/home)
Automatically manage teams for tablist, prefix, winner calculation

### [AchievementManager](./achievement/AchievementManager)
Create custom achievements and give them to players. 

### [MapManager](./map/MapManager)
Use the MapVoting Handler or MapRotation Handler

### [ReplaySystem](./replay/home)
How replays work and how you can manually record replays.
> **Important**: Replays will normally recorded automatically when using GameStateHandler. 
> There is no need to implement this manually!