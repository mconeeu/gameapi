package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;

public class BoatListener extends GadgetListener {

    public BoatListener(GamePlugin plugin) {
        super(plugin);
    }

    public static HashMap<Player, Integer> boatId = new HashMap<>();


    @EventHandler
    public void onBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (p.getItemInHand().getType().equals(Material.BOAT)) {
                if (p.hasPermission("lobby.silenthub")) {
                    p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
                } else {
                    p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
                }
                if (e.getClickedBlock().getType().equals(Material.STATIONARY_WATER) || e.getClickedBlock().getType().equals(Material.WATER)) {
                    Boat boat = (Boat) p.getWorld().spawnEntity(e.getClickedBlock().getLocation(), EntityType.BOAT);
                    p.playSound(p.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
                    boatId.put(p, boat.getEntityId());
                    Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
                        boat.setPassenger(p);
                        CoreSystem.getInstance().createActionBar()
                                .message("§f§oBenutze LSHIFT um auszusteigen")
                                .send(p);
                    }, 1L);
                } else {
                    GameAPI.getInstance().getMessenger().send(p, "§4Du kannst das §cGadget§4 nur im §cWasser §4benutzen!");
                }
            }
        }
    }

    @EventHandler
    public void onBoatdDestory(VehicleBlockCollisionEvent e) {
        e.getVehicle().remove();
    }

    @EventHandler
    public void onBoatSpawn(VehicleCreateEvent e) {
        Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
            if (!boatId.containsValue(e.getVehicle().getEntityId())) {
                e.getVehicle().remove();
            }
        }, 1L);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().getType().equals(EntityType.BOAT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(VehicleDamageEvent e) {
        e.setCancelled(true);
    }


    @EventHandler
    public void onBoatDisamount(EntityDismountEvent e) {
        Player p = (Player) e.getEntity();

        if (e.getDismounted().getType().equals(EntityType.BOAT)) {
            e.getDismounted().remove();

            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
            boatId.remove(p);
        }
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        if (cmd.equalsIgnoreCase("/rl") || cmd.equalsIgnoreCase("/reload")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                World world = all.getWorld();
                for (Boat boat : world.getEntitiesByClass(Boat.class)) {
                    if (boatId.get(all).equals(boat.getEntityId())) {
                        if (boatId.containsKey(all)) {
                            boat.remove();
                            boatId.remove(all);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUnload(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        World world = p.getWorld();
        for (Player all : Bukkit.getOnlinePlayers()) {
            for (Boat boat : world.getEntitiesByClass(Boat.class)) {
                if (boatId.get(all).equals(boat.getEntityId())) {
                    if (boatId.containsKey(all)) {
                        boat.remove();
                        boatId.remove(p);
                    }
                }
            }
        }
    }
}

