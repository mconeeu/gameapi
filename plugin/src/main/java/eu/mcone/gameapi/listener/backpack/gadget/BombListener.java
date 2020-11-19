/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BombListener extends GadgetListener {

    public BombListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem()
                && e.getItem().equals(DefaultItem.BOMB.getItemStack())
                && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))
        ) {
            Player p = e.getPlayer();
            org.bukkit.entity.Item item = p.getLocation().getWorld().dropItem(
                    p.getEyeLocation(),
                    new ItemStack(Material.MAGMA_CREAM)
            );
            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);


            Vector v = p.getLocation().getDirection().multiply(1);
            item.setVelocity(v);

            //TODO: getWorld().playEffect() umwandeln in player.playEffect()

            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);

                    if (player.getLocation().distance(item.getLocation()) <= 6 && gp.getSettings().isEnableGadgets() && gp.isEffectsVisible()) {
                        player.getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY(), item.getLocation().getZ(), 3, false, false);
            /*    item.getWorld().playEffect(item.getLocation().add(0, 1, 0), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(1, 0, 0), Effect.EXPLOSION_LARGE, 1);
                item.getWorld().playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 1); */
                        player.playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 1);
                        player.playEffect(item.getLocation().add(0, 0, 1), Effect.LAVA_POP, 1);
                        player.playEffect(item.getLocation().add(0, 1, 1), Effect.LAVA_POP, 1);
                        player.playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_HUGE, 10);
                        player.playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 10);
                        player.playEffect(item.getLocation().add(0, 0, 1), Effect.EXPLOSION_LARGE, 10);
                        player.playEffect(item.getLocation(), Effect.LAVA_POP, 1);
                        player.playEffect(item.getLocation(), Effect.LAVA_POP, 1);

                        Vector v1 = player.getLocation().getDirection().setY(0.8).multiply(1.1);
                        handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            p.playSound(item.getLocation(), org.bukkit.Sound.FIREWORK_TWINKLE, 1, 1);
                        }, 3));

                        player.setVelocity(v1);
                    }
                }

                for (Entity e1 : item.getWorld().getEntities()) {
                    if (e1.getType().equals(EntityType.DROPPED_ITEM)) {
                        e1.remove();
                    }
                }

                item.getWorld().playEffect(item.getLocation(), Effect.FIREWORKS_SPARK, 1);
                item.getWorld().playSound(item.getLocation(), org.bukkit.Sound.GLASS, 1, 1);
            }, 20));

            Sound.click(p);

            handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Sound.tick(p);

                handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Sound.tick(p);

                    handler.register(e, () -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Sound.done(p);
                        p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.BOMB.getItemStack());

                        handler.cleanup(e);
                    }, 30));
                }, 10));
            }, 10));
        }
    }

}
