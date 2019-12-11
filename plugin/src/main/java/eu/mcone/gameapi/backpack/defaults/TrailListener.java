package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.backpack.handler.GameTrailHandler;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

public class TrailListener extends BackpackInventoryListener {

    @Getter
    private static final GameTrailHandler handler = new GameTrailHandler(plugin);

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GameAPIPlayer<?> gamePlayer, Player p) {
        handler.setTrail(p, item);
        p.closeInventory();
        plugin.getMessager().send(p, "§7Du hast den Trail §f" + item.getName() + "§7 aktiviert!");
    }

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GameAPIPlayer<?> gamePlayer, Player p) {
        super.setBackpackItems(inv, category, categoryItems, gamePlayer, p);

        inv.setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER, 1, 0).displayName("§c§lTrail deaktivieren").lore("§7§oFalls du einen deiner Trails", "§7§oaktiviert hast, kannst Du ihn", "§7§ohiermit deaktivieren.").create(), e -> {
            handler.removeTrail(p);
            plugin.getMessager().send(p, "§cDu hast dein aktuellen Trail erfolgreich deaktiviert!");
        });
    }

}