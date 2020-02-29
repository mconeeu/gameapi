package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.replay.session.ReplaySession;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReplayInformationInventory extends CoreInventory {

    public ReplayInformationInventory(Player player, ReplaySession session) {
        super("§eReplay", player, InventorySlot.ROW_3, InventoryOption.FILL_EMPTY_SLOTS);

        setItem(InventorySlot.ROW_1_SLOT_5, new ItemBuilder(Material.COMMAND, 1).displayName("§7Replay ID: §e" + session.getID()).create());
        setItem(InventorySlot.ROW_3_SLOT_3, new ItemBuilder(Material.GRASS, 1).displayName("§7Maps").lore("§7" + session.getInfo().getWorld()).create());
        setItem(InventorySlot.ROW_3_SLOT_4, new ItemBuilder(Material.WOOL, 1).displayName("§7Winner Team").lore(session.getInfo().getWinnerTeam()).create());
        setItem(InventorySlot.ROW_3_SLOT_5, ItemBuilder.wrap(CategoryInventory.UP_ITEM).displayName("§7Server gestartet").lore("§7" + new SimpleDateFormat("DD:MM:YYYY HH:mm").format(new Date(session.getInfo().getStarted() * 1000))).create());
        setItem(InventorySlot.ROW_3_SLOT_6, ItemBuilder.wrap(CategoryInventory.UP_ITEM).displayName("§7Server gestoppt").lore("§7" + new SimpleDateFormat("DD:MM:YYYY HH:mm").format(new Date(session.getInfo().getStopped() * 1000))).create());
        setItem(InventorySlot.ROW_3_SLOT_7, new ItemBuilder(Material.WATCH).displayName("§eLänge").lore("§f" + session.getInfo().getLength()  / 60 + " §7Minuten").create());
    }
}
