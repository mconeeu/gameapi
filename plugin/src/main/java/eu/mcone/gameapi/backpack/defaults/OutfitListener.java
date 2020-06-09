package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.handler.GameOutfitHandler;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

public class OutfitListener extends BackpackInventoryListener {

    @Getter
    private static final GameOutfitHandler handler = new GameOutfitHandler();

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        handler.setOutfit(p, item);
        p.closeInventory();
        plugin.getMessenger().send(p, "§7Du hast das §f" + item.getName() + "§7 angezogen!");
        p.setWalkSpeed(0.20F);
    }

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gamePlayer, Player p) {
        super.setBackpackItems(inv, category, categoryItems, gamePlayer, p);

        inv.setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER).displayName("§c§lOutfit ausziehen").lore("§7§oFalls du eines deiner Outfits", "§7§oangezogen hast, kannst Du es", "§7§ohiermit ausziehen.").create(), e -> {
            p.getInventory().setArmorContents(null);
            plugin.getMessenger().send(p, "§7Du hast dein Outfit erfolgreich ausgezogen!");
            p.closeInventory();
            p.setWalkSpeed(0.20F);
        });
    }

}
