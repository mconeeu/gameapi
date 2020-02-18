package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GamePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(CorePlayerLoadedEvent e) {
        GameAPIPlayer player = new GameAPIPlayer(e.getPlayer());

        GameAPIPlugin.getSystem().registerGamePlayer(player);
        GameAPIPlugin.getSystem().getServer().getPluginManager().callEvent(new GamePlayerLoadedEvent(e, e.getBukkitPlayer(), e.getPlayer(), player));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerQuitEvent e) {
        GameAPIPlugin.getSystem().unregisterGamePlayer(GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer()));
    }

}
