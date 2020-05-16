package eu.mcone.gameapi.listener.gamestate;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.gamestate.GameStateManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

@RequiredArgsConstructor
public class GameStateListener implements Listener {

    private final GameStateManager manager;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(GamePlayerLoadedEvent e) {
        Player player = e.getPlayer().getCorePlayer().bukkit();

        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().addPlayer(player);
            GamePlugin.getGamePlugin().getReplaySession().getReplayPlayer(player).getData().setJoined(System.currentTimeMillis() / 1000);
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
                PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

                for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                    GamePlugin.getGamePlugin().getMessenger().sendTransl(cps.bukkit(), "game.join", player.getName(), playerManager.getPlayers(GamePlayerState.PLAYING).size(), playerManager.getMaxPlayers());

                    CoreSystem.getInstance().createTitle()
                            .stay(5)
                            .fadeIn(2)
                            .fadeOut(2)
                            .title(GamePlugin.getGamePlugin().getGamemode() != null ? GamePlugin.getGamePlugin().getGamemode().getLabel() : null)
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps, player.getName(), playerManager.getPlayers(GamePlayerState.PLAYING).size(), playerManager.getMaxPlayers()))
                            .send(cps.bukkit());
                }

                try {
                    e.getCorePlayer().getScoreboard().setNewObjective(LobbyGameState.getObjective().newInstance());

                    for (CorePlayer cp : CoreSystem.getInstance().getOnlineCorePlayers()) {
                        if (cp.getScoreboard() != null) {
                            if (cp.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                                cp.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                            }
                        }
                    }
                } catch (InstantiationException | IllegalAccessException instantiationException) {
                    instantiationException.printStackTrace();
                }

                if (!manager.isCountdownRunning() && playerManager.getPlayers(GamePlayerState.PLAYING).size() >= playerManager.getMinPlayers()) {
                    manager.startCountdown(false, 60);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getPlayer());
            gamePlayer.removeFromGame();

            if (manager.getRunning() instanceof LobbyGameState) {
                if (manager.isCountdownRunning() && playerManager.getPlayers(GamePlayerState.PLAYING).size() < playerManager.getMinPlayers()) {
                    manager.cancelCountdown();
                    playerManager.getPlayers(GamePlayerState.PLAYING).forEach((player) -> player.setLevel(GamePlugin.getGamePlugin().getGameStateManager().getCountdownCounter()));
                }
            }
        }
    }

    @EventHandler
    public void onWin(TeamWonEvent e) {
        //Starting EndGamestate
        for (GameState gameState : manager.getPipeline()) {
            if (gameState instanceof EndGameState) {
                manager.setGameState(gameState, true);
                return;
            }
        }
    }

}
