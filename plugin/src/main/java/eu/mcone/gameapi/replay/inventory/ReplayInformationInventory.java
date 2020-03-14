package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReplayInformationInventory extends CoreInventory {

    public ReplayInformationInventory(Player player, ReplaySession session) {
        super("§eReplay", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_1_SLOT_5, new ItemBuilder(Material.COMMAND, 1).displayName("§eReplay ID: §f§l" + session.getID()).create());
        setItem(InventorySlot.ROW_3_SLOT_3, new ItemBuilder(Material.GRASS, 1).displayName("§eMaps").lore("§7" + session.getInfo().getWorld()).create());
        setItem(InventorySlot.ROW_3_SLOT_4, new ItemBuilder(Material.WOOL, 1).displayName("§eWinner Team").lore("§f§l" + session.getInfo().getWinnerTeam()).create());
        setItem(InventorySlot.ROW_3_SLOT_5, ItemBuilder.wrap(CategoryInventory.UP_ITEM).displayName("§eServer gestartet").lore("§f§l" + new SimpleDateFormat("HH:mm").format(new Date(session.getInfo().getStarted() * 1000))).create());
        setItem(InventorySlot.ROW_3_SLOT_6, ItemBuilder.wrap(CategoryInventory.UP_ITEM).displayName("§eServer gestoppt").lore("§f§l" + new SimpleDateFormat("HH:mm").format(new Date(session.getInfo().getStopped() * 1000))).create());
        setItem(InventorySlot.ROW_3_SLOT_7, new ItemBuilder(Material.WATCH).displayName("§eLänge").lore("§f§l" + getLength(session.getInfo().getLastTick())).create());

        openInventory();
    }

    private String getLength(int lastTick) {
        double seconds = lastTick / 30;
        if (seconds < 60) {
            return seconds + " §7Sekunden";
        } else {
            return Double.parseDouble(String.format(Locale.GERMAN, "%1.2f", seconds / 20)) + " §7Minuten";
        }
    }
}
