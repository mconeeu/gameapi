package eu.mcone.gameapi.listener.gamestate;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.gamestate.GameStateStopEvent;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.EndGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
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

    @EventHandler
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

            CoreWorld lobbyWorld = CoreSystem.getInstance().getWorldManager().getWorld(
                    GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()
            );
            if (lobbyWorld != null) {
                lobbyWorld.teleportSilently(p, "spawn");
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
                // TODO: implement new replay recorder
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
                PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

                for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                    GamePlugin.getGamePlugin().getMessenger().sendTransl(cps.bukkit(), "game.join", p.getName(), playerManager.getPlayers(GamePlayerState.PLAYING).size(), playerManager.getMaxPlayers());

                    CoreSystem.getInstance().createTitle()
                            .stay(5)
                            .fadeIn(2)
                            .fadeOut(2)
                            .title(GamePlugin.getGamePlugin().getGameLabel())
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

                    GamePlugin.getGamePlugin().getPlayerManager().getPlayers(GamePlayerState.PLAYING).forEach(
                            player -> GamePlugin.getGamePlugin().getMessenger().send(player, "§2Das Spieler-Minimum wurde erreicht. Der Countdown startet!")
                    );
                }
                if (playerManager.getPlayers(GamePlayerState.PLAYING).size() == playerManager.getMaxPlayers()) {
                    manager.updateCountdownCounter(10);
                }
            }
        }
    }

    @EventHandler
    public void onLobbyStop(GameStateStopEvent e) {
        if (e.getCurrent() instanceof LobbyGameState) {
            GadgetListener.getHandler().stop();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            // TODO: implement new replay recorder
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

            if (manager.getRunning() instanceof LobbyGameState) {
                if (manager.isCountdownRunning() && (playerManager.getPlayers(GamePlayerState.PLAYING).size()-1) < playerManager.getMinPlayers()) {
                    manager.cancelCountdown();
                    playerManager.getPlayers(GamePlayerState.PLAYING).forEach((player) -> {
                        GamePlugin.getGamePlugin().getMessenger().send(player, "§4Der Countdown wurde abgebrochen, da zu wenig Spieler online sind!");
                        player.setLevel(0);
                    });
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWin(TeamWonEvent e) {
        if (!e.isCancelled()) {
            //Starting EndGamestate
            for (GameState gameState : manager.getPipeline()) {
                if (gameState instanceof EndGameState) {
                    ((EndGameState) gameState).setWinnerTeam(e.getTeam());
                    manager.setGameState(gameState, true);
                    return;
                }
            }
        }
    }

}
