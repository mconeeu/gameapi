package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.BackpackItemCategory;
import eu.mcone.gameapi.listener.backpack.gadget.*;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

@Getter
@Setter
public class GadgetListener extends BackpackInventoryListener {

    @Getter
    private static final GameGadgetHandler handler = new GameGadgetHandler();

    static {
        plugin.registerEvents(
                new BombListener(plugin, handler),
                new MakesListener(plugin, handler),
                new CoinBombListener(plugin, handler),
                new EasterGunListener(plugin, handler),
                //new EnderGunListener(plugin),
                new LoveGunListener(plugin, handler),
                new SnowGunListener(plugin, handler),
                new CobwebGunListener(plugin, handler),
                new BoatListener(plugin, handler),
                new FurnaceListener(plugin, handler),
                new FlyOneCarpetListener(plugin, handler),
                new GrapplerListener(plugin, handler),
                new EnderPearlListener(plugin, handler),
                new SplashPotionListener(plugin, handler)
        );
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        if (gamePlayer.getSettings().isEnableGadgets()) {
            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), item.getItem());
            } else {
                p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), item.getItem());
            }

            gamePlayer.setLastUsedBackPackItem(item, DefaultCategory.GADGET.getName());
        } else {
            p.closeInventory();
            GameAPI.getInstance().getMessenger().send(p, "Du kannst keine Gadgets benutzen, da du sie deaktiviert hast. Aktiviere sie wieder in den §fEinstellungen§7!");
        }
    }

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GamePlayer gamePlayer, Player p) {
        super.setBackpackItems(inv, category, categoryItems, gamePlayer, p);

            if (gamePlayer.getLastUsedBackPackItem() != null && gamePlayer.getLastUsedBackPackItem().getCategory().equalsIgnoreCase(DefaultCategory.GADGET.getName())) {
                inv.addCustomPlacedItem(InventorySlot.ROW_6_SLOT_8, new ItemBuilder(Material.BARRIER).displayName("§c§lGadget entfernen").lore("§7§oFalls du einen der Gadgets", "§7§oimmer noch im Inventar", "§7§omit dir trägst.").create(), e -> {

                    if (p.hasPermission("lobby.silenthub")) {
                        p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), null);
                    } else {
                        p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), null);
                    }

                    plugin.getMessenger().send(p, "§7Du hast dein Gadget erfolgreich zurück in deinem Rucksack gelegt!");
                    gamePlayer.removeLastUsedBackPackItem();

                });
            }
    }

}
