package eu.mcone.gameapi.api.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Kit {

    private final String name, description;
    private final ItemStack item;
    private final Map<Integer, ItemStack> kitItems;
    private final int coinsPrice;

    public Kit(String name, String description, ItemStack item, Map<Integer, ItemStack> kitItems) {
        this(name, description, item, kitItems, 0);
    }

    public Kit(String name, String description, ItemStack item, int coinsPrice) {
        this(name, description, item, new HashMap<>(), coinsPrice);
    }

    public Kit(String name, String description, ItemStack item) {
        this(name, description, item, 0);
    }

    public Kit withItem(int slot, ItemStack item) {
        kitItems.put(slot, item);
        return this;
    }

    public ItemStack getItem(int slot) {
        return kitItems.getOrDefault(slot, null);
    }

}
