package eu.mcone.gamesystem.api.game.manager.kit;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import org.bukkit.entity.Player;

import java.util.List;

public interface KitManager {

    List<Kit> getKits();

    void registerKit(final Kit kit);

    void registerKits(final Kit... kits);

    void removeKit(final Kit kit);

    void removeKit(final String kitName);

    void removeKit(final int kitID);

    int getKitID(final String kitName);

    String getKitName(final int kitID);

    Kit getKit(final int kitID);

    Kit getKit(final String kitName);

    void openKitsInventory(Player player);

    void openKitsInventory(Player player, CoreInventory returnInventory);

    void openKitSortingInventory(Player player);
}
