package eu.mcone.gameapi.listener.gamestate;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.api.scoreboard.LobbyObjective;
import eu.mcone.gameapi.backpack.defaults.GadgetListener;
import eu.mcone.gameapi.gamestate.GameStateManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

@RequiredArgsConstructor
public class GameStateListener implements Listener {

    private final GameStateManager manager;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(GamePlayerLoadedEvent e) {
        Player p = e.getPlayer().getCorePlayer().bukkit();

        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
            p.setGameMode(GameMode.SURVIVAL);

            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setLevel(0);
            p.setExp(0);
            p.removePotionEffect(PotionEffectType.SLOW);

            if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
                GamePlugin.getGamePlugin().getReplay().addPlayer(p);
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
                PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

                for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                    GamePlugin.getGamePlugin().getMessenger().sendTransl(cps.bukkit(), "game.join", p.getName(), playerManager.getPlayers(GamePlayerState.PLAYING).size(), playerManager.getMaxPlayers());

                    CoreSystem.getInstance().createTitle()
                            .stay(5)
                            .fadeIn(2)
                            .fadeOut(2)
                            .title(GamePlugin.getGamePlugin().getGamemode() != null ? GamePlugin.getGamePlugin().getGamemode().getLabel() : null)
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps, p.getName(), playerManager.getPlayers(GamePlayerState.PLAYING).size(), playerManager.getMaxPlayers()))
                            .send(cps.bukkit());
                }

                try {
                    LobbyObjective objective = LobbyGameState.getObjective().newInstance();
                    e.getCorePlayer().getScoreboard().setNewObjective(objective);

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
            } else if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                GadgetListener.getHandler().stop();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            if (!GamePlugin.getGamePlugin().getReplayManager().getRecording().isEmpty()) {
                GamePlugin.getGamePlugin().getReplay().removePlayer(e.getPlayer());
            }
        }

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
