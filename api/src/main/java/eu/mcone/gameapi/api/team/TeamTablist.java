package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.scoreboard.Team;

public class TeamTablist extends CoreScoreboard {
    @Override
    public org.bukkit.scoreboard.Team modifyTeam(CorePlayer owner, CorePlayer p, Team t) {
        GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(p.bukkit());
        if (gamePlayer.getTeam() != null) {
            t.setPrefix(gamePlayer.getTeam().getName().getChatColor().toString());
        }
        return t;
    }
}
