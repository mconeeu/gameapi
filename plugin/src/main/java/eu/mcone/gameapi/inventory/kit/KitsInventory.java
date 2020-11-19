package eu.mcone.gameapi.inventory.kit;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
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
            boolean isCurrentKit = gp.getCurrentKit() != null && gp.getCurrentKit().equals(kit);

            ItemBuilder item = ItemBuilder
                    .wrap(kit.getItem())
                    .addLore("");

            if (isCurrentKit) {
                item.addLore("§8» §f§nRechtsklick§8 | §7§oKitslots modifizieren");
            } else {
                item.addLore("§8» §f§nRechtsklick§8 | §7§oKit kaufen");
                item.addLore("§8» §f§nShiftklick§8 | §7§oKitslots modifizieren");
            }

            setItem(
                    slot,
                    item.create(),
                    e -> {
                        if (isCurrentKit || e.isShiftClick()) {
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

        if (!gp.getCurrentKit().equals(GamePlugin.getGamePlugin().getKitManager().getDefaultKit())) {
            setAutoBuyKitItem(getInventory().getSize()-3, p, gp);
        }
        setItem(
                getInventory().getSize()-2,
                ItemBuilder.wrap(REFRESH_ITEM)
                        .displayName("§f§lKit sortieren")
                        .lore("§7§oSortiere die Slots deines", "§7§oaktuellen Kits")
                        .create(),
                e -> new KitSortInventory(p, gp, manager, gp.getCurrentKit(), onBackClick)
        );
        setItem(getInventory().getSize()-1, BACK_ITEM, e -> onBackClick.run());

        openInventory();
    }

    private static int getInvSize(int kits) {
        if (kits <= 6) {
            return InventorySlot.ROW_1;
        } else if (kits <= InventorySlot.ROW_4) {
            int rows = (int) Math.ceil((double) kits / InventorySlot.ROW_1);
            return (rows + 2) * InventorySlot.ROW_1;
        } else throw new IllegalArgumentException("Kits Inventory cannot display more than "+InventorySlot.ROW_5+" kits!");
    }

    private void setAutoBuyKitItem(int slot, Player p, GamePlayer gp) {
        setItem(
                slot,
                Skull.fromUrl("http://textures.minecraft.net/texture/5a6787ba32564e7c2f3a0ce64498ecbb23b89845e5a66b5cec7736f729ed37")
                        .toItemBuilder()
                        .displayName("§a§lKits nach dem Tod")
                        .lore(
                                "§a§lautomatisch kaufen",
                                gp.isAutoBuyKit() ? "§cAktiviert" : "§7§lDeaktiviert",
                                "",
                                "§7§oKlicke hier um dein letztes Kit",
                                "§7§onach dem Tod automatisch wieder zu",
                                "§7§ozu kaufen."
                        )
                        .create(),
                e -> {
                    gp.setAutoBuyKit(!gp.isAutoBuyKit());

                    setAutoBuyKitItem(slot, p, gp);
                    p.updateInventory();
                }
        );
    }

}
