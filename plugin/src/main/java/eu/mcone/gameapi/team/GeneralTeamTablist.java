package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboardEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;

public class GeneralTeamTablist extends CoreScoreboard {

    @Override
    public void modifyTeam(CorePlayer owner, CorePlayer player, CoreScoreboardEntry t) {
        GamePlayer gp = GamePlugin.getGamePlugin().getGamePlayer(player.getUuid());

        if (gp.getTeam() != null) {
            Team team = gp.getTeam();
            t.priority(team.getPriority()).prefix(team.getChatColor().toString());
        } else {
            t.prefix("§4x §8").priority(9999);
        }
    }

}
