package eu.mcone.gameapi.api.broadcast;

import eu.mcone.coresystem.api.bukkit.broadcast.Broadcast;
import eu.mcone.coresystem.api.bukkit.broadcast.BroadcastMessage;
import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WinBroadcast extends Broadcast {

    private String teamName;

    public WinBroadcast(Team team) {
        super(new BroadcastMessage(
                "game.team.won",
                new Object[]{team.getLabel()}
        ));

        this.teamName = team.getName();
    }

}
