package eu.mcone.gameapi.api.kit;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public interface KitManager {

    int getKitsChooserItemSlot();

    ItemStack getKitChooserItem();

    void reload();

    void registerKits(Kit... kit);

    void setDefaultKit(Kit kit);

    Kit getDefaultKit();

    void removeKit(String name);

    Kit getKit(String name);

    void openKitsInventory(Player p, Runnable onBackClick);

    ModifiedKit getModifiedKit(Player p, String name);

    boolean hasKitModified(Player p, String name);

    void modifyKit(Player player, Kit kit, Map<ItemStack, Integer> items);

    Map<Integer, ItemStack> calculateItems(Kit kit, Player player);

}
