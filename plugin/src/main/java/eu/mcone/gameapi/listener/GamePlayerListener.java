package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
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
        Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(e, e.getBukkitPlayer(), p, gp));
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        GameAPIPlugin.getSystem().unregisterGamePlayer(
                GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer())
        );
    }

}