package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class GeneralItemListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPickUp(PlayerPickupItemEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getPlayer());

            if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDrop(PlayerDropItemEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getPlayer());

            if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }
}
