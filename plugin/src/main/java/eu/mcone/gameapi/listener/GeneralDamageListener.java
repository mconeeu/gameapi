package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class GeneralDamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer((Player) e.getEntity());
                Player damager = (Player) e.getDamager();
                Player player = (Player) e.getEntity();
                if (gamePlayer != null && gamePlayer.getTeam() != null) {
                    if (gamePlayer.getTeam().getPlayers().contains(damager)) {
                        e.setCancelled(true);
                    }
                }

                if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(damager)
                        || GamePlugin.getGamePlugin().getPlayerManager().isSpectator(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();

                if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
