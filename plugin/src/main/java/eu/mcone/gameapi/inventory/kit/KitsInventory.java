package eu.mcone.gameapi.inventory.kit;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import org.bukkit.entity.Player;

public class KitsInventory extends CoreInventory {

    public KitsInventory(Player p, GameKitManager manager) {
        super("§8» §c§lKits", p, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);
        GamePlayer gp = GameAPIPlugin.getSystem().getGamePlayer(p);

        int slot = getStartSlot(manager.getKits().size());
        for (Kit kit : manager.getKits()) {
            setItem(slot, ItemBuilder.wrap(kit.getItem()).addLore("").addLore(gp.hasKit(kit) ? "§8» §f§nRechtsklick§8 | §7§oKitslots modifizieren" : "§8» §f§nRechtsklick§8 | §7§oKit kaufen").create(), e -> {
                if (gp.hasKit(kit)) {
                    new KitSortInventory(p, gp, manager, kit);
                } else {
                    new KitBuyInventory(p, gp, manager, kit);
                }
            });
            slot++;
        }

        openInventory();
    }

    public int getStartSlot(int kitsAmount) {
        if (/* 1 Rows */ kitsAmount <= 7) {
            return InventorySlot.ROW_3_SLOT_2;
        } else if (/* 2 Rows */ kitsAmount <= 14) {
            return InventorySlot.ROW_3_SLOT_2;
        } else if (/* 3Rows */ kitsAmount <= 21) {
            return InventorySlot.ROW_2_SLOT_2;
        } else if (/* 4 Rows */ kitsAmount <= 28) {
            return InventorySlot.ROW_2_SLOT_2;
        }

        return InventorySlot.ROW_1_SLOT_2;
    }

}
