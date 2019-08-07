package eu.mcone.gamesystem.api.game.manager.kit;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Kit {

    private int kitID;
    private String name;
    private String displayName;
    private ItemStack kitItem;
    private KitItem[] kitItems;
    private int coins;

    public Kit(final int kitID, final String name, final String displayName, final ItemStack kitItem, final KitItem[] kitItems, final int coins) {
        this.kitID = kitID;
        this.name = name;
        this.displayName = displayName;
        this.kitItem = kitItem;
        this.kitItems = kitItems;
        this.coins = coins;
    }

    public Kit(final int kitID, final String name, final String displayName, final ItemStack kitItem, final KitItem[] kitItems) {
        this.kitID = kitID;
        this.name = name;
        this.displayName = displayName;
        this.kitItem = kitItem;
        this.kitItems = kitItems;
        this.coins = 0;
    }

    public KitItem getKitItem(final double kitItemID) {
        for (KitItem kitItem : kitItems) {
            if (kitItem.getKitItemID() == kitItemID) {
                return kitItem;
            }
        }

        return null;
    }

    public KitItem getKitItem(final ItemStack itemStack) {
        for (KitItem kitItem : kitItems) {
            if (kitItem.getItemStack().equals(itemStack)) {
                return kitItem;
            }
        }

        return null;
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();

        for (KitItem kitItem : kitItems) {
            items.add(kitItem.getItemStack());
        }

        return items;
    }
}
