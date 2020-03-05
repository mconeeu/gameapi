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

    public KitsInventory(Player p, GameKitManager manager, Runnable onBackClick) {
        super("§8» §c§lKits", p, getInvSize(manager.getKits().size()), InventoryOption.FILL_EMPTY_SLOTS);
        GamePlayer gp = GameAPIPlugin.getSystem().getGamePlayer(p);

        int slot = 0;
        for (Kit kit : manager.getKits()) {
            setItem(slot, ItemBuilder.wrap(kit.getItem()).addLore("").addLore((manager.isApplyKitsOnce() ? manager.getCurrentKit(p).equals(kit) : gp.hasKit(kit)) ? "§8» §f§nRechtsklick§8 | §7§oKitslots modifizieren" : "§8» §f§nRechtsklick§8 | §7§oKit kaufen").create(), e -> {
                if (gp.getCurrentKit() != null && gp.getCurrentKit().getName().equals(kit.getName())) {
                    new KitSortInventory(p, gp, manager, kit, onBackClick);
                } else {
                    if (kit.getCoinsPrice() > 0) {
                        new KitBuyInventory(p, gp, manager, kit, onBackClick);
                    } else {
                        gp.buyKit(kit);
                        p.closeInventory();
                    }
                }
            });
            slot++;
        }

        setItem(getBackItemSlot(manager.getKits().size()), BACK_ITEM, e -> onBackClick.run());

        openInventory();
    }

    private static int getBackItemSlot(int kits) {
        if (kits <= 7) {
            return InventorySlot.ROW_1_SLOT_9;
        } else if (kits <= 16) {
            return InventorySlot.ROW_2_SLOT_9;
        } else if (kits <= 25) {
            return InventorySlot.ROW_3_SLOT_9;
        } else if (kits <= 34) {
            return InventorySlot.ROW_4_SLOT_9;
        } else if (kits <= 43) {
            return InventorySlot.ROW_5_SLOT_9;
        } else {
            return InventorySlot.ROW_6_SLOT_9;
        }
    }

    private static int getInvSize(int kits) {
        if (kits <= 7) {
            return InventorySlot.ROW_1;
        } else if (kits <= 16) {
            return InventorySlot.ROW_2;
        } else if (kits <= 25) {
            return InventorySlot.ROW_3;
        } else if (kits <= 34) {
            return InventorySlot.ROW_4;
        } else if (kits <= 43) {
            return InventorySlot.ROW_5;
        } else {
            return InventorySlot.ROW_6;
        }
    }

}
