/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.HashSet;

public class BoatListener extends GadgetListener {

    public BoatListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    public static HashMap<Player, Integer> boatId = new HashMap<>();
    public static HashSet<Integer> boatlist = new HashSet<>();

    @EventHandler
    public void onBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (p.getItemInHand().getType().equals(Material.BOAT)) {
                p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);

                if (e.getClickedBlock().getType().equals(Material.STATIONARY_WATER) || e.getClickedBlock().getType().equals(Material.WATER)) {
                    Boat boat = (Boat) p.getWorld().spawnEntity(e.getClickedBlock().getLocation(), EntityType.BOAT);
                    Sound.play(p, org.bukkit.Sound.FIREWORK_TWINKLE);
                    boatId.put(p, boat.getEntityId());
                    boatlist.add(boat.getEntityId());
                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
                        boat.setPassenger(p);
                        CoreSystem.getInstance().createActionBar()
                                .message("§f§oBenutze LSHIFT um auszusteigen")
                                .send(p);

                        handler.cleanup(e);
                    }, 1L));
                } else {
                    Msg.send(p, "§4Du kannst das §cGadget§4 nur im §cWasser §4benutzen!");
                }
            }
        }
    }

    @EventHandler
    public void onBoatdDestory(VehicleBlockCollisionEvent e) {
        if (boatlist.contains(e.getVehicle().getEntityId())) {
            e.getVehicle().remove();
            boatlist.remove(e.getVehicle().getEntityId());
        }
    }

    @EventHandler
    public void onBoatSpawn(VehicleCreateEvent e) {
        handler.register(e, () -> Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
            if (!boatId.containsValue(e.getVehicle().getEntityId())) {
                e.getVehicle().remove();
            }

            handler.cleanup(e);
        }, 1L));
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
            if (boatId.containsKey(p)) {
                if (boatlist.contains(e.getDismounted().getEntityId())) {
                    e.getDismounted().remove();

                    Sound.cancel(p);
                    boatId.remove(p);
                    boatlist.remove(e.getDismounted().getEntityId());
                }
            }
        }
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        if (cmd.equalsIgnoreCase("/rl") || cmd.equalsIgnoreCase("/reload")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                World world = all.getWorld();
                for (Boat boat : world.getEntitiesByClass(Boat.class)) {
                    if (boatId.containsKey(all)) {
                        if (boatId.get(all).equals(boat.getEntityId())) {
                            if (boatlist.contains(boat.getEntityId())) {
                                boat.remove();
                                boatId.remove(all);
                                boatlist.remove(boat.getEntityId());
                            }
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
                if (all != null) {
                    if (boatId.containsKey(all)) {
                        if (boatId.get(all).equals(boat.getEntityId())) {
                            if (boatlist.contains(boat.getEntityId())) {
                                boat.remove();
                                boatId.remove(p);
                                boatlist.remove(boat.getEntityId());
                            }
                        }
                    }
                }
            }
        }
    }
}

