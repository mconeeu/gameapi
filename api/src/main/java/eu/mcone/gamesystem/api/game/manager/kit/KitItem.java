package eu.mcone.gamesystem.api.game.manager.kit;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class KitItem {

    private final double kitItemID;
    private final KitItemType kitItemType;
    private final ItemStack itemStack;

    public KitItem(final double kitItemID, final KitItemType kitItemType, final ItemStack itemStack) {
        this.kitItemID = kitItemID;
        this.kitItemType = kitItemType;
        this.itemStack = itemStack;
    }
}
