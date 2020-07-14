package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.GadgetScheduler;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class MakesListener extends GadgetListener {


    public MakesListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    private final HashMap<Player, Location> playerLocation = new HashMap<>();
    private final ArrayList<Player> isUse = new ArrayList<>();


    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.hasItem() && e.getItem().equals(DefaultItem.MAKES.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            for (Player otherplayer : player.getWorld().getPlayers()) {
                if (otherplayer != null) {
                    if (player.hasLineOfSight(otherplayer) && player.getLocation().distance(otherplayer.getLocation()) < 5.5 && player != otherplayer) {
                        GamePlayer go = GameAPI.getInstance().getGamePlayer(otherplayer);
                        if (!isUse.contains(player)) {
                            if (go.getSettings().isEnableGadgets() && go.isEffectsVisible()) {
                                playerLocation.put(otherplayer, otherplayer.getLocation());
                                final float xdiff = (float) (otherplayer.getLocation().getX() - player.getLocation().getX());
                                final float zdiff = (float) (otherplayer.getLocation().getZ() - player.getLocation().getZ());
                                final float average = Math.abs((xdiff + zdiff) / 1.9f);
                                final float x = xdiff / average;
                                final float z = zdiff / average;
                                otherplayer.setVelocity(new Vector(x / 2.0f, 2.8f, z / 2.0f));
                                otherplayer.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);

                                isUse.add(player);

                                if (player.hasPermission("lobby.silenthub")) {
                                    player.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
                                } else {
                                    player.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
                                }

                                for (Player players : Bukkit.getOnlinePlayers()) {
                                    players.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 5);
                                    players.playEffect(player.getLocation(), Effect.WITCH_MAGIC, 20);
                                    players.playEffect(player.getLocation(), Effect.CLICK1, 20);
                                    players.playEffect(player.getLocation(), Effect.SMALL_SMOKE, 5);
                                    handler.register(new GadgetScheduler() {
                                        @Override
                                        public BukkitTask register() {
                                            return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                players.playEffect(otherplayer.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
                                                players.playEffect(otherplayer.getLocation(), Effect.LARGE_SMOKE, 5);
                                                handler.register(new GadgetScheduler() {
                                                    @Override
                                                    public BukkitTask register() {
                                                        return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                            otherplayer.setVelocity(new Vector(x / 1.6f, 1.8f, z / 1.9f));
                                                            players.playEffect(otherplayer.getLocation(), Effect.EXPLOSION_LARGE, 5);
                                                            otherplayer.playSound(otherplayer.getLocation(), Sound.EXPLODE, 1, 1);
                                                            handler.register(new GadgetScheduler() {
                                                                @Override
                                                                public BukkitTask register() {
                                                                    return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                                        otherplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 43, 1));
                                                                        otherplayer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 42, 1));
                                                                        handler.register(new GadgetScheduler() {
                                                                            @Override
                                                                            public BukkitTask register() {
                                                                                return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                                                    if (playerLocation.containsKey(otherplayer)) {
                                                                                        otherplayer.teleport(playerLocation.get(otherplayer));
                                                                                        playerLocation.remove(otherplayer);
                                                                                        otherplayer.playSound(otherplayer.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                                                                                    }

                                                                                    handler.register(new GadgetScheduler() {
                                                                                        @Override
                                                                                        public BukkitTask register() {
                                                                                            return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                                                                isUse.remove(player);
                                                                                                handler.remove(this);
                                                                                            }, 120L);
                                                                                        }
                                                                                    });

                                                                                    handler.remove(this);
                                                                                }, 9L);
                                                                            }
                                                                        });

                                                                        handler.remove(this);
                                                                    }, 7L);
                                                                }
                                                            });

                                                            handler.remove(this);
                                                        }, 12L);
                                                    }
                                                });

                                                handler.remove(this);
                                            }, 9L);
                                        }
                                    });

                                }
                            }
                        } else {
                            GameAPI.getInstance().getMessenger().send(player, "§4Bitte warte ein paar Sekunden...");
                        }
                    }
                } else {
                    GameAPI.getInstance().getMessenger().send(player, "§4Es ist kein anderer §cSpieler§4 Online!");
                }
            }
        }
    }
}
