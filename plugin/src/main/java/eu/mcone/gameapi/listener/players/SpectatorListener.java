package eu.mcone.gameapi.listener.players;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.player.GamePlayerManager;
import eu.mcone.gameapi.team.GameTeamManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class SpectatorListener implements Listener {

    private final GamePlayerManager manager;

    @EventHandler
    public void on(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);

        if (gp != null) {
            if (e.getRightClicked().getType().equals(EntityType.PLAYER)) {
                Player t = (Player) e.getRightClicked();
                GamePlayer tgp = GameAPI.getInstance().getGamePlayer(t);

                if (tgp != null) {
                    if (gp.getState().equals(GamePlayerState.SPECTATING) && tgp.getState().equals(GamePlayerState.PLAYING)) {
                        gp.setInCameraMode(t);
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);

        if (gp != null) {
            if (gp.getState().equals(GamePlayerState.SPECTATING) && gp.isInCameraMode()) {
                gp.removeFromCameraMode();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack i = e.getItem();
            if ((i == null) || (!i.hasItemMeta()) || (!i.getItemMeta().hasDisplayName())) {
                return;
            }

            if (i.equals(PlayerManager.SPECTATOR)) {
                GamePlugin.getGamePlugin().getPlayerManager().openSpectatorInventory(p);
            } else if (i.equals(GameTeamManager.TEAM)) {
                if (!GamePlugin.getGamePlugin().getTeamManager().isTeamsFinallySet()) {
                    GamePlugin.getGamePlugin().getTeamManager().openTeamInventory(p);
                } else {
                    GameAPI.getInstance().getMessenger().send(p, "§4Du kannst dein Team nicht mehr ändern!");
                }
            }
        }

        if (GameAPI.getInstance().getGamePlayer(p).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        moveSpectatorsOutOf(e.getBlock(), p);

        if (GameAPI.getInstance().getGamePlayer(p).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(BlockBreakEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerDropItemEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerPickupItemEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && GameAPI.getInstance().getGamePlayer((Player) e.getDamager()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && GameAPI.getInstance().getGamePlayer((Player) e.getEntity()).getState().equals(GamePlayerState.SPECTATING)) {
            e.setCancelled(true);
        }
    }

    private void moveSpectatorsOutOf(Block block, Player builder) {
        Set<Player> spectators = manager.getPlayers(GamePlayerState.SPECTATING);

        if (!spectators.isEmpty()) {
            Location blockLocation = block.getLocation();
            Collection<Entity> players = block.getLocation().getWorld().getNearbyEntities(block.getLocation(), blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());

            for (Entity entity : players) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (player != builder && spectators.contains(player)) {
                        Location playerLocation = player.getLocation();
                        Location eyeLocation = player.getEyeLocation();

                        if ((blockLocation.getX() == playerLocation.getX()
                                && blockLocation.getY() == playerLocation.getY()
                                && blockLocation.getZ() == playerLocation.getZ()
                        ) || (blockLocation.getX() == eyeLocation.getX()
                                && eyeLocation.getY() == eyeLocation.getY()
                                && eyeLocation.getZ() == eyeLocation.getZ()
                        )) {
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
    }

}
