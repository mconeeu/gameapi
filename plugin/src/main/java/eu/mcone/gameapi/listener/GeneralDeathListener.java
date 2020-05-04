package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GeneralDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerDeathEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getEntity());
            if (GamePlugin.getGamePlugin().getTeamManager().isExitBySingleDeath()
                    || !gamePlayer.getTeam().isAlive()) {
                gamePlayer.removeFromGame();
            }

            if (!GamePlugin.getGamePlugin().getTeamManager().isWinMethodDeactivated()) {
                Team team = GamePlugin.getGamePlugin().getTeamManager().checkChanceToWin();
                if (team != null) {
                    Bukkit.getPluginManager().callEvent(new TeamWonEvent(team));

                    for (GamePlayer player : GamePlugin.getGamePlugin().getOnlineGamePlayers()) {
                        GamePlugin.getGamePlugin().getPlayerManager().setPlaying(player.bukkit(), false);
                        GamePlugin.getGamePlugin().getPlayerManager().setSpectating(player.bukkit(), false);
                    }
                }
            }
        }
    }
}
