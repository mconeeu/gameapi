package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.event.player.GamePlayerUnloadEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GamePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();
        GameAPIPlayer gp = new GameAPIPlayer(p);

        GameAPIPlugin.getSystem().registerGamePlayer(gp);

        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
                GamePlugin.getGamePlugin().getPlayerManager().setPlaying(p.bukkit(), true);
            } else if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                GamePlugin.getGamePlugin().getPlayerManager().setSpectating(p.bukkit(), true);
            }
        }

        Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(e, e.getBukkitPlayer(), p, gp));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerQuitEvent e) {
        GameAPIPlayer gamePlayer = GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer());
        Bukkit.getPluginManager().callEvent(new GamePlayerUnloadEvent(gamePlayer.bukkit(), gamePlayer.getCorePlayer(), gamePlayer));

        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                gamePlayer.removeFromGame();

                Team team = GamePlugin.getGamePlugin().getTeamManager().checkChanceToWin();
                if (team != null) {
                    Bukkit.getPluginManager().callEvent(new TeamWonEvent(team));

                    for (Player player : GamePlugin.getGamePlugin().getPlayerManager().getPlaying()) {
                        GamePlugin.getGamePlugin().getPlayerManager().setPlaying(player, false);
                        GamePlugin.getGamePlugin().getPlayerManager().setSpectating(player, false);
                    }
                }
            }
        }

        GameAPIPlugin.getSystem().unregisterGamePlayer(
                GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer())
        );
    }
}