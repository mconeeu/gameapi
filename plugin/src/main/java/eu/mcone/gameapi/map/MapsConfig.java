package eu.mcone.gameapi.map;

import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.map.GameAPIMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@NoArgsConstructor
public class MapsConfig {

    private List<GameAPIMap> maps;

    public MapsConfig addWorld(CoreWorld world, ItemStack item) {
        maps.add(new GameAPIMap(world.getName(), item));
        return this;
    }

    public boolean containsMap(String name) {
        for (GameAPIMap map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

}
