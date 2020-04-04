package eu.mcone.gameapi.listener.gamestate;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameStateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(GamePlayerLoadedEvent e) {
        GamePlayer gamePlayer = e.getPlayer();
        Player player = e.getPlayer().getCorePlayer().bukkit();

        System.out.println("GAMESYSTEM");
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().addPlayer(player);
            GamePlugin.getGamePlugin().getReplaySession().getReplayPlayer(player).getData().setJoined(System.currentTimeMillis() / 1000);
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
                PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

                for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                    GamePlugin.getGamePlugin().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.join", cps)
                            .replace("%player%", player.getName())
                            .replace("%playing%", Integer.toString(playerManager.getPlaying().size()))
                            .replace("%max%", String.valueOf(playerManager.getMaxPlayers())));

                    CoreSystem.getInstance().createTitle()
                            .stay(5)
                            .fadeIn(2)
                            .fadeOut(2)
                            .title(CoreSystem.getInstance().getTranslationManager().get("game.prefix", cps))
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps)
                                    .replace("%player%", player.getName())
                                    .replace("%playing%", Integer.toString(playerManager.getPlaying().size()))
                                    .replace("%max%", String.valueOf(playerManager.getMaxPlayers())))
                            .send(cps.bukkit());
                }

                GameStateManager gameStateManager = GamePlugin.getGamePlugin().getGameStateManager();
                if (!gameStateManager.isCountdownRunning() && playerManager.getPlaying().size() >= playerManager.getMinPlayers()) {
                    gameStateManager.startCountdown(false, 60);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
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
