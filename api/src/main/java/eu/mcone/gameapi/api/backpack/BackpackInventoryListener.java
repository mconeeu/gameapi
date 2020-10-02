package eu.mcone.gameapi.api.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

@RequiredArgsConstructor
public abstract class BackpackInventoryListener {

    @Setter
    protected static GamePlugin plugin;
    private final String categoryItemsName;

    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player player) {}

    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gp, Player p) {
        for (BackpackItem item : categoryItems) {
            if (gp.hasBackpackItem(category.getName(), item) || (!category.getName().equals("STORY_ITEMS") && p.hasPermission("system.game.items"))) {
                if (!plugin.getBackpackManager().getDisabledItems().contains(item.getName())) {
                    inv.addItem(item.getItem(), e -> {
                        if (gp.isEffectsVisible()) {
                            onBackpackInventoryClick(item, gp, p);
                        } else {
                            p.closeInventory();
                            GameAPI.getInstance().getMessenger().send(p, "§4Du kannst keine Effekte benutzen, da Effekte von anderen für dich unsichtbar sind");
                        }
                    });
                }
            }
        }

        if (gp.getCurrentBackpackItem() != null && gp.getCurrentBackpackItem().getCategory().name().equals(category.getName())) {
            inv.addCustomPlacedItem(
                    InventorySlot.ROW_6_SLOT_8,
                    new ItemBuilder(Material.BARRIER, 1, 0)
                            .displayName("§c§l"+categoryItemsName+" deaktivieren")
                            .lore("§7§oDeaktiviere dein", "§7§oaktuelles Rucksack", "§7§oItem.")
                            .create(),
                    e -> removeCurrentItem(gp.getCurrentBackpackItem().getBackpackItem(), gp, p)
            );
        }
    }

    private void removeCurrentItem(BackpackItem item, GamePlayer gp, Player p) {
        gp.resetCurrentBackpackItem();
        p.closeInventory();

        onItemItemRemove(item, gp, p);
        plugin.getMessenger().sendSuccess(p, "Du hast das "+categoryItemsName+" !["+item.getName()+"] erfolgreich deaktiviert!");
    }

    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {}

}
