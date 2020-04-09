package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Collection;

public class GeneralBlockListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getPlayer());

            if (!GamePlugin.getGamePlugin().getPlayerManager().getSpectating().isEmpty()) {
                Location blockLocation = e.getBlock().getLocation();
                Collection<Entity> players = e.getBlock().getLocation().getWorld().getNearbyEntities(e.getBlock().getLocation(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());

                for (Entity entity : players) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(player)) {
                            Location playerLocation = player.getLocation();
                            Location eyeLocation = player.getEyeLocation();
                            if (blockLocation.getX() == playerLocation.getX() && blockLocation.getY() == playerLocation.getY() && blockLocation.getZ() == playerLocation.getZ()
                                    || blockLocation.getX() == eyeLocation.getX() && eyeLocation.getY() == eyeLocation.getY() && eyeLocation.getZ() == eyeLocation.getZ()) {
                                boolean stop = true;
                                for (int x = 1; stop; x++) {
                                    playerLocation.setX(x);
                                    if (playerLocation.getBlock().getType() == Material.AIR) {
                                        stop = false;
                                    }
                                }

                                player.teleport(playerLocation);
                            }
                        }
                    }
                }
            }

            if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockBreakEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(e.getPlayer());

            if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }


}
