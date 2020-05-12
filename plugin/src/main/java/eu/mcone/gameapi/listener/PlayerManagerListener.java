package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.player.GamePlayerManager;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@RequiredArgsConstructor
public class PlayerManagerListener implements Listener {
    
    private final GamePlayerManager manager;
    
    @EventHandler
    public void on(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player) && !manager.getInCamera().contains(player)) {
            if (e.getRightClicked() instanceof Player) {
                Player target = (Player) e.getRightClicked();

                PacketPlayOutCamera camera = new PacketPlayOutCamera();
                camera.a = target.getEntityId();
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(camera);
                manager.getInCamera().add(player);
                GamePlugin.getGamePlugin().getMessenger().send(player, "§7Du bist nun in der Ansicht des Spielers §f" + player.getName());
            }
        }
    }

    @EventHandler
    public void on(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player) && manager.getInCamera().contains(player)) {
            if (e.isSneaking()) {
                PacketPlayOutCamera camera = new PacketPlayOutCamera();
                camera.a = player.getEntityId();
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(camera);
                manager.getInCamera().remove(player);
                GamePlugin.getGamePlugin().getMessenger().send(player, "§7Du bist nun nicht mehr in der Ansicht des Spielers §f" + player.getName());
            }
        }
    }

    @EventHandler
    public void on(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (manager.getSpectating().contains(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (manager.getSpectating().contains(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (manager.getSpectating().contains(player)) {
                e.setCancelled(true);
            }
        }
    }
    
}
