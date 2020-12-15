/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack.handler;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface PetHandler {


    void spawnPet(Player p, BackpackItem item);

    boolean hasPet(Player p);

    Entity getEntity(Player p);

    Collection<Entity> getPets();

    void despawnPet(Player p);
}
