package eu.mcone.gameapi.api.map;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class GameAPIMap {

    private String name;
    private Material item;

    public CoreWorld getWorld() {
        return CoreSystem.getInstance().getWorldManager().getWorld(name);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(name);
    }

}
