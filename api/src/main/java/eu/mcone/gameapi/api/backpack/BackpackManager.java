package eu.mcone.gameapi.api.backpack;

import eu.mcone.gameapi.api.backpack.handler.OutfitHandler;
import eu.mcone.gameapi.api.backpack.handler.PetHandler;
import eu.mcone.gameapi.api.backpack.handler.TrailHandler;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;

public interface BackpackManager {

    boolean isUseRankBoots();

    void setUseRankBoots(boolean use);

    void registerCategory(Category category, Set<BackpackItem> items, BackpackInventoryListener listener) throws IllegalArgumentException;

    void registerCategory(Category category, Set<BackpackItem> items) throws IllegalArgumentException;

    void loadAdditionalCategories(String... names);

    void reload();

    BackpackItem getBackpackItem(String category, int id);

    void openBackpackInventory(String category, Player player);

    void openBackpackSellInventory(String name, Player p) throws IllegalArgumentException;

    Map<Category, Set<BackpackItem>> getBackpackItems(String... categories);

    boolean categoryExists(String category);

    boolean itemExists(BackpackItem item);

    /**
     * Stops all running Backpackmanager effects (like trails and gadgets) and resets the inventory of every player
     */
    void stop();

    PetHandler getPetHandler();

    TrailHandler getTrailHandler();

    OutfitHandler getOutfitHandler();

    void setRankBoots(Player p);
}
