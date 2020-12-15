/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.handler;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.backpack.handler.PetHandler;
import eu.mcone.gameapi.listener.backpack.PetTrackListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GamePetHandler implements PetHandler {

    private final HashMap<UUID, Entity> pets;

    public GamePetHandler(GamePlugin plugin) {
        pets = new HashMap<>();
        plugin.registerEvents(
                new PetTrackListener(this)
        );
    }

    @Override
    public void spawnPet(Player p, BackpackItem item) {
        DefaultItem animal = DefaultItem.getItemByID(DefaultCategory.PET, item.getId());

        if (animal != null) {
            despawnPet(p);
            EntityType type = null;

            switch (animal) {
                case ANIMAL_PIG:
                    type = EntityType.PIG;
                    break;
                case ANIMAL_WITHER:
                    type = EntityType.WITHER;
                    break;
                case ANIMAL_SHEEP:
                    type = EntityType.SHEEP;
                    break;
                case ANIMAL_MUSHROOM_COW:
                    type = EntityType.MUSHROOM_COW;
                    break;
                case ANIMAL_CHICKEN:
                    type = EntityType.CHICKEN;
                    break;
            }

            Entity entity = p.getWorld().spawnEntity(p.getLocation(), type);
            entity.setCustomName("§b§o" + p.getName() + "s "+ item.getName());
            entity.setCustomNameVisible(true);

            pets.put(p.getUniqueId(), entity);
        } else {
            throw new IllegalStateException("Could not spawn Animal from item "+item.getName()+". Item is not an animal!");
        }
    }

    @Override
    public boolean hasPet(Player p) {
        return pets.containsKey(p.getUniqueId());
    }

    @Override
    public Entity getEntity(Player p) {
        return pets.getOrDefault(p.getUniqueId(), null);
    }

    @Override
    public Collection<Entity> getPets() {
        return pets.values();
    }

    public void renamePet(Player p, String name) {
        if (pets.containsKey(p.getUniqueId())) {
            pets.get(p.getUniqueId()).setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        }
    }

    public void ride(Player p) {
        if (pets.containsKey(p.getUniqueId())) {
            pets.get(p.getUniqueId()).setPassenger(p);
            CoreSystem.getInstance().createActionBar()
                    .message("§f§oBenutze LSHIFT um abszusteigen")
                    .send(p);
        }
    }

    @Override
    public void despawnPet(Player p) {
        if (pets.containsKey(p.getUniqueId())) {
            pets.get(p.getUniqueId()).remove();
            pets.remove(p.getUniqueId());
        }
    }

    public void followPlayer(Player p) {
        Location location = p.getLocation();
        Creature creature = (Creature) pets.get(p.getUniqueId());

        if (location.distanceSquared(creature.getLocation()) > 100) {
            if (((Entity) p).isOnGround()) {
                creature.teleport(p);
            }
        } else {
            ((CraftCreature) creature).getHandle().getNavigation().a(location.getX(), location.getY(), location.getZ(), 1.4);
        }
    }

}
