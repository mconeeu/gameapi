/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.BackpackSchedulerProvider;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class CoinBombListener extends GadgetListener {

    public CoinBombListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }


    public static boolean isExploding = false;
    private final HashSet<Location> bombLocation = new HashSet<>();


    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.COINBOMB.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();
            GameAPIPlayer gp = GameAPIPlugin.getSystem().getGamePlayer(p);


            if (isExploding) {
                GameAPI.getInstance().getMessenger().send(p, "§4Bitte warte kurz..");
                return;
            }
            isExploding = true;
            p.getInventory().remove(p.getItemInHand());
            DefaultItem.COINBOMB.remove(gp);
            p.sendMessage("§aDu hast die Coin Bombe erfolgreich gezündet!");


            for (GamePlayer all : GameAPI.getInstance().getOnlineGamePlayers()) {
                Set<Location> locations = new HashSet<>();

                org.bukkit.entity.Item item = p.getLocation().getWorld().dropItem(
                        p.getEyeLocation(),
                        new ItemStack(Material.TNT, 1)
                );
                    p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);

                Vector v = p.getLocation().getDirection().multiply(0.7);
                item.setVelocity(v);


                handler.register(e, new BackpackSchedulerProvider() {
                    @Override
                    public BukkitTask register() {
                        return Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            item.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY(),
                                    item.getLocation().getZ(), 3, false, false);

                            locations.add(item.getLocation());
                            all.bukkit().spigot().playEffect(item.getLocation(), Effect.LAVADRIP, 1, 1, 1, 1, 1, 2, 35, 10);
                            all.bukkit().spigot().playEffect(item.getLocation(), Effect.LAVA_POP, 1, 1, 1, 1, 1, 2, 35, 12);

                            for (Location location : locations) {
                                TNTPrimed tnt = item.getWorld().spawn(location, TNTPrimed.class);
                                tnt.setFuseTicks(68);

                                handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                    all.bukkit().sendMessage("§8[§7§l!§8] §fServer §8» §7Eine Coin Bombe wurde von §f" + p.getName() + " §7gezündet sie explodiert in §f3 Sekunden");
                                }, 20L));

                                handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                    all.bukkit().sendMessage("§8[§7§l!§8] §fServer §8» §7Sie startet in §f2 Sekunden");
                                }, 40L));

                                all.bukkit().spigot().playEffect(item.getLocation(), Effect.SPLASH, 1, 1, 1, 1, 1, 1, 100, 10);

                                handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                    all.bukkit().sendMessage("§8[§7§l!§8] §fServer §8» §7Die Coinbome von §f" + p.getName() + " §7wird in §f1 Sekunde §7gezündet!");

                                    all.bukkit().spigot().playEffect(item.getLocation(), Effect.WITCH_MAGIC, 1, 1, 1, 1, 1, 3, 35, 7);


                                    handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                                        all.bukkit().sendMessage("§8[§7§l!§8] §fServer §8» §7Die §fCoin Bombe§7 ist §f§lexplodiert§7 hebe die Items auf!");
                                        handler.cleanup(e);
                                    }, 12L));
                                }, 60L));
                            }
                        }, 40L);
                    }
                });
            }
        }

    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        e.blockList().clear();


        Location tnt = e.getLocation();

        org.bukkit.entity.Item item1 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.GOLD_BLOCK));

        org.bukkit.entity.Item item2 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.GOLD_INGOT));

        org.bukkit.entity.Item item3 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.DIAMOND_BLOCK));

        org.bukkit.entity.Item item4 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.IRON_BLOCK));

        org.bukkit.entity.Item item5 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.EMERALD_BLOCK));

        org.bukkit.entity.Item item6 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.EMERALD));

        org.bukkit.entity.Item item7 = tnt.getWorld().dropItem(tnt,
                new ItemStack(Material.EMERALD));

        Vector v1 = tnt.getDirection().setY(0.8).setZ(0.3);
        Vector v2 = tnt.getDirection().setY(1).setZ(-0.2);
        Vector v3 = tnt.getDirection().setY(0.8).setX(-0.2).setZ(0.3);
        Vector v4 = tnt.getDirection().setY(0.9).setX(0.3);
        Vector v5 = tnt.getDirection().setY(0.7).setZ(0.3);
        Vector v6 = tnt.getDirection().setY(0.8).setX(-0.3);
        Vector v7 = tnt.getDirection().setY(1.5).setX(0.5).setZ(-0.2);
        Vector v8 = tnt.getDirection().setY(1.7).setX(0.6).setZ(0.2);
        Vector v9 = tnt.getDirection().setY(0.6).setX(-0.1).setZ(0.5);
        Vector v10 = tnt.getDirection().setY(0.9).setX(-0.7).setZ(-0.4);
        Vector v11 = tnt.getDirection().setY(2.9).setX(-0.9).multiply(0.4).setZ(-0.5);
        Vector v12 = tnt.getDirection().setY(0.9).multiply(0.3).setX(0.1).setZ(0.7).multiply(0.6);
        Vector v13 = tnt.getDirection().setY(0.8).setZ(0.8).multiply(2);
        Vector v14 = tnt.getDirection().setY(0.8).setZ(-0.9).multiply(0.5);

        item1.setVelocity(v1);
        item2.setVelocity(v2);
        item3.setVelocity(v3);
        item4.setVelocity(v4);
        item5.setVelocity(v5);
        item6.setVelocity(v6);
        item7.setVelocity(v7);
        item1.setVelocity(v8);
        item2.setVelocity(v9);
        item3.setVelocity(v10);

        item4.setVelocity(v11);
        item5.setVelocity(v12);
        item6.setVelocity(v13);
        item7.setVelocity(v14);

        item3.setVelocity(v6);

        handler.register(e, () -> Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
            for (Entity e1 : item1.getWorld().getEntities()) {
                if (e1.getType().equals(EntityType.DROPPED_ITEM)) {
                    e1.remove();

                    isExploding = false;

                }
            }

            handler.cleanup(e);
        }, 205L));

    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Item item = e.getItem();

        if (isExploding) {
            if (item.getItemStack().getType().equals(Material.IRON_INGOT) || item.getItemStack().getType().equals(Material.IRON_BLOCK)) {
                GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(player);
                gamePlayer.getCorePlayer().addCoins(100);
                GameAPI.getInstance().getMessenger().send(player, "§7Du hast ein §fCoin Bomben§7 Item aufgesammelt §8[§a+100 Coins§8]");
            } else if (item.getItemStack().getType().equals(Material.EMERALD) || item.getItemStack().getType().equals(Material.EMERALD_BLOCK)) {
                GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(player);
                gamePlayer.getCorePlayer().addEmeralds(5);
                GameAPI.getInstance().getMessenger().send(player, "§7Du hast ein §fCoin Bomben§7 Item aufgesammelt §8[§a+5 Emeralds§8]");
            } else if (item.getItemStack().getType().equals(Material.GOLD_BLOCK) || item.getItemStack().getType().equals(Material.GOLD_INGOT) || item.getItemStack().getType().equals(Material.DIAMOND_BLOCK)) {
                GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(player);
                gamePlayer.getCorePlayer().addCoins(150);
                GameAPI.getInstance().getMessenger().send(player, "§7Du hast ein §fCoin Bomben§7 Item aufgesammelt §8[§a+150 Coins§8]");
            }
            Sound.done(player);
            player.playEffect(player.getLocation(), Effect.FLAME, 5);
            item.remove();
        }
    }

}