package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Modules;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameStateListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (GamePlugin.getGamePlugin().getModules().contains(Modules.PLAYER_MANAGER) && GamePlugin.getGamePlugin().getModules().contains(Modules.GAME_STATE_MANAGER)) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();
            GameStateManager gameStateManager = GamePlugin.getGamePlugin().getGameStateManager();

            if (gameStateManager.getRunning() instanceof LobbyGameState) {
                if (!gameStateManager.isCountdownRunning() && playerManager.getPlaying().size() >= playerManager.getMinPlayers()) {
                    gameStateManager.startCountdown(false, 20);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GamePlugin.getGamePlugin().getModules().contains(Modules.PLAYER_MANAGER) && GamePlugin.getGamePlugin().getModules().contains(Modules.GAME_STATE_MANAGER)) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();
            GameStateManager gameStateManager = GamePlugin.getGamePlugin().getGameStateManager();

            if (gameStateManager.getRunning() instanceof LobbyGameState) {
                if (gameStateManager.isCountdownRunning() && playerManager.getPlaying().size() < playerManager.getMinPlayers()) {
                    gameStateManager.cancelCountdown();
                    playerManager.getPlaying().forEach((player) -> player.setLevel(LobbyGameState.getLobbyCountdown()));
                }
            }
        }
    }

}
