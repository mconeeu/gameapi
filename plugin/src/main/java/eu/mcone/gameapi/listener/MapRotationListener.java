package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.map.GameMapRotationHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class MapRotationListener implements Listener {

    private final GameMapRotationHandler handler;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        handler.getCurrentMap().getWorld().teleportSilently(e.getPlayer(), "spawn");
    }

}
