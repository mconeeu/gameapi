package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.backpack.handler.GameTrailHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

@RequiredArgsConstructor
public class TrailListener implements Listener {

    private final GameTrailHandler handler;

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        handler.unregisterPlayer(e.getPlayer());
    }

    public void onStop(PluginDisableEvent e) {
        handler.stop();
    }

}
