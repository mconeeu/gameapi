/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class FireWorkListener extends GadgetListener {

    public FireWorkListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    public boolean isExploding = false;


    @EventHandler(priority = EventPriority.LOWEST)
    public void on(BlockPlaceEvent e) {

        if (e.getItemInHand().equals(DefaultItem.FIREWORK.getItemStack())) {

            e.getBlockPlaced().setType(DefaultItem.FIREWORK.getItemStack().getType());

            System.out.println("hello2");
            Player p = e.getPlayer();
            GameAPIPlayer gp = GameAPIPlugin.getSystem().getGamePlayer(p);

            if (isExploding) {
                GameAPI.getInstance().getMessenger().send(p, "§4Bitte warte kurz..");
                e.setCancelled(true);
                return;
            }
            isExploding = true;


            DefaultItem.FIREWORK.remove(gp);
            p.sendMessage("§aDu hast die Silvester Batterie erfolgreich gezündet!");


            eu.mcone.coresystem.api.bukkit.util.Firework firework = new eu.mcone.coresystem.api.bukkit.util.Firework();

            for (GamePlayer all : GameAPI.getInstance().getOnlineGamePlayers()) {

                firework.color(Color.AQUA).fade(Color.RED).with(FireworkEffect.Type.BALL).trail(true).flicker(false).spawn(e.getBlockPlaced().getLocation(), 9);


                all.bukkit().playEffect(e.getBlockPlaced().getLocation(), Effect.FIREWORKS_SPARK, 10);


                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {

                    firework.color(Color.AQUA).fade(Color.RED).with(FireworkEffect.Type.BALL).trail(true).flicker(false).spawn(e.getBlockPlaced().getLocation(), 9);
                    all.bukkit().playEffect(e.getBlockPlaced().getLocation(), Effect.FIREWORKS_SPARK, 10);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                        firework.color(Color.AQUA).fade(Color.RED).with(FireworkEffect.Type.BALL).trail(true).flicker(false).spawn(e.getBlockPlaced().getLocation(), 9);
                        all.bukkit().playEffect(e.getBlockPlaced().getLocation(), Effect.FIREWORKS_SPARK, 10);
                    }, 20L));

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                        firework.color(Color.AQUA).fade(Color.RED).with(FireworkEffect.Type.BALL).trail(true).flicker(false).spawn(e.getBlockPlaced().getLocation(), 9);
                        all.bukkit().playEffect(e.getBlockPlaced().getLocation(), Effect.FIREWORKS_SPARK, 10);
                    }, 40L));

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                        firework.color(Color.AQUA).fade(Color.RED).with(FireworkEffect.Type.BALL).trail(true).flicker(false).spawn(e.getBlockPlaced().getLocation(), 9);
                        all.bukkit().playEffect(e.getBlockPlaced().getLocation(), Effect.FIREWORKS_SPARK, 10);

                        handler.register(e, () -> Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                            e.getBlockPlaced().setType(Material.AIR);
                            isExploding = false;
                            handler.cleanup(e);
                        }, 12L));
                    }, 60L));
                }, 40L));
            }
        }
    }
}
