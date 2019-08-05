package eu.mcone.gamesystem.game.manager.kit;

import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import eu.mcone.gamesystem.game.manager.kit.sorting.SortKitsInventory;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitManager implements eu.mcone.gamesystem.api.game.manager.kit.KitManager {

    @Getter
    private List<Kit> kits;

    public KitManager() {
        kits = new ArrayList<>();
    }

    public void registerKit(final Kit kit) {
        if (!kits.contains(kit)) {
            kits.add(kit);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cThe kit with the name " + kit.getName() + " is already registered!");
        }
    }

    public void registerKits(final Kit... kits) {
        for (Kit kit : kits) {
            registerKit(kit);
        }
    }

    public void removeKit(final Kit kit) {
        kits.remove(kit);
    }

    public void removeKit(final String kitName) {
        Kit toRemove = null;
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                toRemove = kit;
                break;
            }
        }

        if (toRemove != null) {
            kits.remove(toRemove);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cThe kit with the name " + kitName + " doesn't exists");
        }
    }

    public void removeKit(final int kitID) {
        Kit toRemove = null;
        for (Kit kit : kits) {
            if (kit.getKitID() == kitID) {
                toRemove = kit;
                break;
            }
        }

        if (toRemove != null) {
            kits.remove(toRemove);
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cThe kit with the kitID " + kitID + " doesn't exists");
        }
    }

    public int getKitID(final String kitName) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                return kit.getKitID();
            }
        }

        return 0;
    }

    public String getKitName(final int kitID) {
        for (Kit kit : kits) {
            if (kit.getKitID() == kitID) {
                return kit.getName();
            }
        }

        return "Not found";
    }

    public Kit getKit(final int kitID) {
        for (Kit kit : kits) {
            if (kit.getKitID() == kitID) {
                return kit;
            }
        }

        return null;
    }

    public Kit getKit(final String kitName) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }

        return null;
    }

    public void openKitsInventory(Player player) {
        new KitsInventory(player);
    }

    public void openKitSortingInventory(Player player) {
        new SortKitsInventory(player);
    }
}
