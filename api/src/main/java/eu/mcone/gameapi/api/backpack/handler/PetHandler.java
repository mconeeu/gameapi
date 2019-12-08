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
