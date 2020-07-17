package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.GadgetScheduler;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FurnaceListener extends GadgetListener {

    public FurnaceListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    private final List<Location> furnaceLocations = new ArrayList<>();

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.MYSTERY_FURNACHE.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {

            List<Location> locations = new ArrayList<>();
            Player p = e.getPlayer();

            org.bukkit.entity.Item item = p.getLocation().getWorld().dropItem(p.getEyeLocation(),
                    new ItemStack(Material.FURNACE));
            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
            } else {
                p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
            }

            Vector v = p.getLocation().getDirection().multiply(1);
            item.setVelocity(v);


//            if ((oldItemDown.getType().equals(Material.GRASS) || oldItemDown.getType().equals(Material.AIR)) && oldItemUp.getType().equals(Material.AIR)) {
            handler.register(new GadgetScheduler() {
                @Override
                public BukkitTask register() {
                    return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY(),
                                item.getLocation().getZ(), 3, false, false);

                /*Block yplus1 = item.getLocation().getWorld().getBlockAt(new Location(item.getWorld(), item.getLocation().getBlockX(), item.getLocation().getBlockY() + 1, item.getLocation().getBlockZ()));
                Block startair = item.getLocation().getWorld().getBlockAt(new Location(item.getWorld(), item.getLocation().getBlockX(), item.getLocation().getBlockY(), item.getLocation().getBlockZ()));
                 */

                        for (int i = 0; i <= 1; i++) {
                            int x;
                            int y;
                            int z;

                            Random random = new Random();
                            x = random.nextInt(1);
                            y = random.nextInt(1);
                            z = random.nextInt(1);
                            locations.add(item.getLocation().add(x, y, z));
                        }


                        for (Location location : locations) {
                            if (item.getLocation().getWorld().getBlockAt(location).getType().equals(Material.AIR)) {
                                for (GamePlayer gp : GameAPI.getInstance().getOnlineGamePlayers()) {
                                    gp.bukkit().getWorld().getBlockAt(location).setType(Material.FURNACE);
                                    furnaceLocations.add(location);

                                    if (!gp.getSettings().isEnableGadgets() || !gp.isEffectsVisible()) {
                                        gp.bukkit().getWorld().getBlockAt(location).setType(Material.AIR);
                                    }
                                }
                            }

                        }


//                    Location loc1 = item.getLocation();
//                    Location loc2 = item.getLocation().add(0, 1, 0);
//                    if (item.getLocation().getWorld().getBlockAt(loc1).getType().equals(Material.AIR)) {
//                        item.getLocation().getWorld().getBlockAt(loc1)
//                                .setType(Material.WEB);
//                    }
//
//                    if (item.getLocation().getWorld().getBlockAt(loc2).getType().equals(Material.AIR)) {
//                        item.getLocation().getWorld().getBlockAt(loc2)
//                                .setType(Material.WEB);
//                    }

                        item.getWorld().playEffect(item.getLocation(), Effect.FIREWORKS_SPARK, 1);
                        item.getWorld().playSound(item.getLocation(), Sound.GLASS, 1, 1);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> item.getWorld().playSound(item.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1), 3);
                    }, 20);
                }
            });

            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
            handler.register(new GadgetScheduler() {
                @Override
                public BukkitTask register() {
                    return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                        handler.register(new GadgetScheduler() {
                            @Override
                            public BukkitTask register() {
                                return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    p.playSound(p.getLocation(), Sound.CLICK, 1, 1);

                                    handler.register(new GadgetScheduler() {
                                        @Override
                                        public BukkitTask register() {
                                            return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);

                                                handler.register(new GadgetScheduler() {
                                                    @Override
                                                    public BukkitTask register() {
                                                        return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
                                                            for (Location location : furnaceLocations) {
                                                                for (GamePlayer gp : GameAPI.getInstance().getOnlineGamePlayers()) {
                                                                    if (gp.getSettings().isEnableGadgets() && gp.isEffectsVisible()) {
                                                                        gp.bukkit().sendBlockChange(location, Material.AIR, (byte) 0);
                                                                    }
                                                                }
                                                            }

                                                            furnaceLocations.clear();
                                                            handler.remove(this);
                                                        }, 20 * 40);
                                                    }
                                                });

                                                handler.remove(this);
                                            }, 18);
                                        }
                                    });

                                    handler.remove(this);
                                }, 10);
                            }
                        });

                        handler.remove(this);
                    }, 10);
                }
            });
//            } else {
//                LobbyPlugin.getInstance().getMessenger().send(p, "§4Die Cobweb gun funktioniert hier nicht (Block im Weg!)");
//                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
//                if (p.hasPermission("lobby.silenthub")) {
//                    p.getInventory().setItem(3, Item.COBWEBGUN.getItemStack());
//                } else {
//                    p.getInventory().setItem(2, Item.COBWEBGUN.getItemStack());
//                }
//            }
        }
    }

}
