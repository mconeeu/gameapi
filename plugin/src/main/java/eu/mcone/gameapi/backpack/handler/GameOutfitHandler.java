/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.handler;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.backpack.handler.OutfitHandler;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GameOutfitHandler implements OutfitHandler {

    @Override
    public void setOutfit(Player p, BackpackItem item) {
        DefaultItem choosed = DefaultItem.getItemByID(DefaultCategory.OUTFIT, item.getId());

        if (choosed != null) {
            switch (choosed) {
                case OUTFIT_RABBIT: {
                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.GRAY).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.GRAY).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.GRAY).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/dc7a317ec5c1ed7788f89e7f1a6af3d2eeb92d1e9879c05343c57f9d863de130", 1).getItemStack());
                    System.out.println("DEBUG-1");
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
                    p.getInventory().setBoots(ItemBuilder.createLeatherArmorItem(Material.LEATHER_BOOTS, Color.RED).create());
                    p.getInventory().setChestplate(ItemBuilder.createLeatherArmorItem(Material.LEATHER_CHESTPLATE, Color.GRAY).create());
                    p.getInventory().setLeggings(ItemBuilder.createLeatherArmorItem(Material.LEATHER_LEGGINGS, Color.RED).create());

                    p.getInventory().setHelmet(Skull.fromUrl("http://textures.minecraft.net/texture/df783877fc2581ead734847a6cea5bfd6e23939616b1e004459332b5b3933bcd", 1).getItemStack());

                }
            }
        } else {
            throw new IllegalStateException("Could not set Outfit from item " + item.getName() + ". Item is not a outfit!");
        }
    }

}
