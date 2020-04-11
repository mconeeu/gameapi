package eu.mcone.gameapi.api.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GameConfig {

    private String lobby = "world";
    private String gameWorld = "world";
    private int maxPlayers = 0;
    private int minPlayers = 0;
    private int playersPerTeam = 0;
    //time in min
    private int season = 45;

}
