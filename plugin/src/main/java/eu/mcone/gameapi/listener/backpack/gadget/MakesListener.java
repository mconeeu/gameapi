/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        Player p = e.getPlayer();
        if (e.hasItem() && e.getItem().equals(DefaultItem.MAKES.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            if (Bukkit.getOnlinePlayers().size() > 1) {
                for (Player otherplayer : p.getWorld().getPlayers()) {
                    if (otherplayer != null) {
                        if (p.hasLineOfSight(otherplayer) && p.getLocation().distance(otherplayer.getLocation()) < 5.5 && p != otherplayer) {
                            GamePlayer go = GameAPI.getInstance().getGamePlayer(otherplayer);
                            if (!isUse.contains(p)) {
                                if (go.getSettings().isEnableGadgets() && go.isEffectsVisible()) {
                                    playerLocation.put(otherplayer, otherplayer.getLocation());
                                    final float xdiff = (float) (otherplayer.getLocation().getX() - p.getLocation().getX());
                                    final float zdiff = (float) (otherplayer.getLocation().getZ() - p.getLocation().getZ());
                                    final float average = Math.abs((xdiff + zdiff) / 1.9f);
                                    final float x = xdiff / average;
                                    final float z = zdiff / average;
                                    otherplayer.setVelocity(new Vector(x / 2.0f, 2.8f, z / 2.0f));
                                    Sound.error(otherplayer);

                                    isUse.add(p);

                                    p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);

                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        players.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);
                                        players.playEffect(p.getLocation(), Effect.WITCH_MAGIC, 20);
                                        players.playEffect(p.getLocation(), Effect.CLICK1, 20);
                                        players.playEffect(p.getLocation(), Effect.SMALL_SMOKE, 5);

                                        handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            players.playEffect(otherplayer.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
                                            players.playEffect(otherplayer.getLocation(), Effect.LARGE_SMOKE, 5);

                                            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                otherplayer.setVelocity(new Vector(x / 1.6f, 1.8f, z / 1.9f));
                                                players.playEffect(otherplayer.getLocation(), Effect.EXPLOSION_LARGE, 5);
                                                Sound.play(otherplayer, org.bukkit.Sound.EXPLODE);

                                                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                    otherplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 43, 1));
                                                    otherplayer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 42, 1));

                                                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                        if (playerLocation.containsKey(otherplayer)) {
                                                            otherplayer.teleport(playerLocation.get(otherplayer));
                                                            playerLocation.remove(otherplayer);
                                                            Sound.teleport(otherplayer);
                                                        }

                                                        handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                            isUse.remove(p);

                                                            handler.cleanup(e);
                                                        }, 120L));
                                                    }, 9L));
                                                }, 7L));
                                            }, 12L));
                                        }, 9L));

                                    }
                                }
                            } else {
                                Msg.send(p, "§4Bitte warte ein paar Sekunden...");
                            }
                        }
                    } else {
                        Msg.send(p, "§4Es ist kein anderer §cSpieler§4 Online!");
                    }
                }
            } else {
                Msg.send(p, "§4Es ist kein anderer §cSpieler§4 Online!");
            }
        }
    }
}
