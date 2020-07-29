package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

import static eu.mcone.gameapi.replay.inventory.ReplayInformationInventory.getLength;

public class ReplayRecordingInventory extends CoreInventory {

    public ReplayRecordingInventory(Player player, ReplayRecord replayRecord) {
        super("§8» Replay", player, InventorySlot.ROW_5, InventoryOption.FILL_EMPTY_SLOTS);

        if (replayRecord != null) {
            setItem(InventorySlot.ROW_3_SLOT_5,
                    new ItemBuilder(replayRecord.getGamemode().getItem(), 1)
                            .displayName("")
                            .lore(replayRecord.getGamemode().getColor() + replayRecord.getGamemode().getName(),
                                    "§l§e" + replayRecord.getRecorder().getRecorderID(),
                                    "§7" + new SimpleDateFormat("yy.MM.dd HH:mm").format(new Date(replayRecord.getRecorder().getStarted() * 1000)),
                                    "",
                                    "§7Map: §e" + replayRecord.getRecorder().getWorld(),
                                    "§7Spieler: §e" + replayRecord.getPlayers().size(),
                                    "",
                                    "§eVor " + getLength(replayRecord.getRecorder().getTicks()) + " gestartet.")
                            .create()
            );
        } else {
            setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.BARRIER, 1).displayName("§cNicht aktiv").create());
        }

        openInventory();
    }
}
