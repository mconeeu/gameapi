package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.player.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GamePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();

        GameAPIPlayer gp = new GameAPIPlayer(p);
        GameAPIPlugin.getSystem().registerGamePlayer(gp);
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
                GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory().addPlayer(p.bukkit());
            }
        }

        Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(e, e.getBukkitPlayer(), p, gp));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (GamePlugin.isGamePluginInitialized()) {
            GameAPI.getInstance().getGamePlayer(e.getPlayer()).removeFromGame();
        }
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
                GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory().getPlayer(player).setLeaved(System.currentTimeMillis() / 1000);
            }
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.USE_REPLAY_VIEW_MANAGER)) {
                ReplayContainer replayContainer = GamePlugin.getGamePlugin().getReplayManager().getReplayViewManager().getContainer(player);
                if (replayContainer != null) {
                    replayContainer.removeViewers(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        GameAPIPlayer gp = GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer());
        gp.saveData();

        GameAPIPlugin.getSystem().unregisterGamePlayer(gp);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setRespawnLocation(
                    CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getGameWorld()).getLocation("game.spectator")
            );
        }
    }

}