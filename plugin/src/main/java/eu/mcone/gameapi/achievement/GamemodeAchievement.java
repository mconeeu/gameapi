package eu.mcone.gameapi.achievement;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.achievement.Achievement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamemodeAchievement {

    private Gamemode gamemode;
    private List<Achievement> achievements;

}
