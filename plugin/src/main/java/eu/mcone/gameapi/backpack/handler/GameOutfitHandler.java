/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.handler;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.backpack.handler.OutfitHandler;
import eu.mcone.gameapi.listener.backpack.gadget.CoinBombListener;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class GameOutfitHandler implements OutfitHandler {

    @Override
    public void setOutfit(Player p, BackpackItem item) {
        DefaultItem choosed = DefaultItem.getItemByID(DefaultCategory.OUTFIT, item.getId());

        if (choosed != null) {
            switch (choosed) {
                case OUTFIT_RABBIT: {
                    Location rabbit1 = p.getLocation().add(7, 6, 1);
                    Location rabbit2 = p.getLocation().add(3, 4, 7);
                    Location rabbit3 = p.getLocation().add(1, 1, 6);
                    Location rabbit4 = p.getLocation().add(6, 2, 3);

                    p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);

                    p.getLocation().getWorld().dropItem(rabbit1, new ItemBuilder(Material.CARROT, 9).create());
                    p.getLocation().getWorld().dropItem(rabbit2, new ItemBuilder(Material.CARROT_ITEM, 5).create());
                    p.getLocation().getWorld().dropItem(rabbit3, new ItemBuilder(Material.CARROT_STICK, 12).create());
                    p.getLocation().getWorld().dropItem(rabbit4, new ItemBuilder(Material.GOLDEN_CARROT, 2).create());

                    Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
                        for (Entity e : p.getWorld().getEntities()) {
                            if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                                if (!CoinBombListener.isExploding) {
                                    e.remove();
                                }
                                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                            }
                        }
                    }, 60L);

                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.GRAY).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.GRAY).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.GRAY).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).getItemStack());
                    break;
                }
                case OUTFIT_DINOSAUR: {

                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.GREEN).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.GREEN).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.SILVER).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/d582ce1d9f6f34c087b4fbec5bdb758732dc0658b86e275a9b46bacd58ae899", 1).getItemStack());
                    break;
                }

                case OUTFIT_SANTA: {
                    Location snow1 = p.getLocation().add(7, 6, 1);
                    Location snow2 = p.getLocation().add(2, 5, 4);
                    Location snow3 = p.getLocation().add(1, 4, 6);
                    Location snow4 = p.getLocation().add(5, 1, 2);

                    p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);

                    p.getLocation().getWorld().dropItem(snow1, new ItemBuilder(Material.SNOW_BALL, 9).create());
                    p.getLocation().getWorld().dropItem(snow2, new ItemBuilder(Material.SNOW, 5).create());
                    p.getLocation().getWorld().dropItem(snow3, new ItemBuilder(Material.SNOW_BALL, 12).create());
                    p.getLocation().getWorld().dropItem(snow4, new ItemBuilder(Material.SNOW_BLOCK, 2).create());

                    Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
                        for (Entity e : p.getWorld().getEntities()) {
                            if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                                if (!CoinBombListener.isExploding) {
                                    e.remove();
                                }
                                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                            }
                        }
                    }, 60L);

                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.RED).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.GRAY).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.RED).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/df783877fc2581ead734847a6cea5bfd6e23939616b1e004459332b5b3933bcd", 1).getItemStack());

                    break;
                }
                case OUTIFT_KRUEMEL_MONSTER: {
                    Location cookie1 = p.getLocation().add(6, 4, 1);
                    Location cookie2 = p.getLocation().add(2, 5, 4);
                    Location cookie3 = p.getLocation().add(1, 4, 6);
                    Location cookie4 = p.getLocation().add(0, 1, 0);

                    p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);

                    p.getLocation().getWorld().dropItem(cookie1, new ItemBuilder(Material.COOKIE, 9).create());
                    p.getLocation().getWorld().dropItem(cookie2, new ItemBuilder(Material.COOKIE, 5).create());
                    p.getLocation().getWorld().dropItem(cookie3, new ItemBuilder(Material.COOKIE, 10).create());
                    p.getLocation().getWorld().dropItem(cookie4, new ItemBuilder(Material.COOKIE, 2).create());

                    Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
                        for (Entity e : p.getWorld().getEntities()) {
                            if (e.getType().equals(EntityType.DROPPED_ITEM)) {
                                if (!CoinBombListener.isExploding) {
                                    e.remove();
                                }
                                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                            }
                        }
                    }, 60L);

                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.YELLOW).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.FUCHSIA).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.YELLOW).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/b592cf9f42a5a8c995968493fdd1b11e0b69aad6473ff45384abe58b7fc7c7", 1).getItemStack());

                }
            }
        } else {
            throw new IllegalStateException("Could not set Outfit from item " + item.getName() + ". Item is not a outfit!");
        }
    }

}
