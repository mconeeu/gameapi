package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

public class HatListener extends BackpackInventoryListener {

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        if (item.getId() == DefaultItem.HEAD_SECRET_STRIPCLUB.getId()) {
            p.getInventory().setHelmet(item.getItem());
            p.closeInventory();
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 Kopf aufgesetzt!");
            p.setWalkSpeed(0.25F);
        } else {
            p.getInventory().setHelmet(item.getItem());
            p.closeInventory();
            plugin.getMessenger().send(p, "§7Du hast den §f" + item.getName() + "§7 Kopf aufgesetzt!");
            p.setWalkSpeed(0.20F);
        }
    }

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gamePlayer, Player p) {
        super.setBackpackItems(inv, category, categoryItems, gamePlayer, p);

        inv.setItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER).displayName("§c§lKopf absetzen").lore("§7§oFalls du einen deiner Köpfe", "§7§oaufgesetzt hast, kannst Du ihn", "§7§ohiermit absetzen.").create(), e -> {
            p.getInventory().setHelmet(null);
            plugin.getMessenger().send(p, "§7Du hast deinen Kopf erfolgreich abgesetzt!");
            p.setWalkSpeed(0.20F);
        });
    }

}
