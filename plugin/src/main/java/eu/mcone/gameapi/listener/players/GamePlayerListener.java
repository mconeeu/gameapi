package eu.mcone.gameapi.listener.players;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.player.GamePlayerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class GamePlayerListener implements Listener {

    private final GamePlayerManager manager;

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        if (manager.maxPlayersReached()) {
            e.getPlayer().kickPlayer("Max Players reached");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLoaded(GamePlayerLoadedEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                e.getPlayer().setState(GamePlayerState.SPECTATING);
            }
        }
    }

}
