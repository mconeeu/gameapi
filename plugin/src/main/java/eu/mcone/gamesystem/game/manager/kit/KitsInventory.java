/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.kit;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.manager.kit.Kit;
import org.bukkit.entity.Player;

public class KitsInventory extends CoreInventory {

    KitsInventory(Player player) {
        super("§8» §e§lHändler §8| §fKits", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);

        int slot = KitsInventory.getStartSlot();
        for (Kit kit : GameTemplate.getInstance().getKitManager().getKits()) {
            setItem(slot, kit.getKitItem(), e -> buyKit(kit));
        }

        openInventory();
    }

    private void buyKit(Kit kit) {
        CorePlayer cp = CoreSystem.getInstance().getCorePlayer(player);
        if (kit.getCoins() != 0) {
            if ((cp.getCoins() - kit.getCoins()) >= 0) {
                cp.removeCoins(kit.getCoins());
                player.getInventory().clear();

                GameTemplate.getInstance().getMessager().send(player, "§2Du hast das §7" + kit.getDisplayName() + " §2erfolgreich für §7" + kit.getCoins() + " §2Coins gekauft");
                player.closeInventory();
            } else {
                GameTemplate.getInstance().getMessager().send(player, "§cDu hast nicht genügend Coins!");
                player.closeInventory();
            }
        }
    }

    public static int getStartSlot() {
        int kitSize = GameTemplate.getInstance().getKitManager().getKits().size();

        //1 Row
        if (kitSize <= 7) {
            return InventorySlot.ROW_3_SLOT_1;
            //2 Rows
        } else if (kitSize <= 14) {
            return InventorySlot.ROW_3_SLOT_1;

            //3 Rows
        } else if (kitSize <= 21) {
            return InventorySlot.ROW_2_SLOT_1;
            //4 Rows
        } else if (kitSize <= 28) {
            return InventorySlot.ROW_2_SLOT_1;
        }

        return InventorySlot.ROW_1_SLOT_1;
    }
}
