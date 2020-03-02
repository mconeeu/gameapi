package eu.mcone.gameapi.listener.map;

import eu.mcone.gameapi.map.GameMapRotationHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class MapRotationListener implements Listener {

    private final GameMapRotationHandler handler;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        handler.getCurrentMap().getWorld().teleportSilently(e.getPlayer(), "spawn");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(handler.getCurrentMap().getWorld().getLocation("spawn"));
    }

}
