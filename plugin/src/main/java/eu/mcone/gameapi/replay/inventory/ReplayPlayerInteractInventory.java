package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReplayPlayerInteractInventory extends CoreInventory {

    public ReplayPlayerInteractInventory(final ReplayPlayer replayPlayer, Player player) {
        super(replayPlayer.getData().getDisplayName(), player, InventorySlot.ROW_4, InventoryOption.FILL_EMPTY_SLOTS);
        CorePlayer cp = CoreSystem.getInstance().getCorePlayer(replayPlayer.getUuid());

        Skull skull = new Skull(replayPlayer.getData().getName());
        setItem(InventorySlot.ROW_1_SLOT_5, skull.lore(
                "§7Name: §f" + replayPlayer.getData().getName(),
                "§7Anzeigename: " + replayPlayer.getData().getDisplayName(),
                "§7Rang: §f" + cp.getMainGroup().getFormattingCode() + cp.getMainGroup().getName(),
                "§7Onlinetime: §f" + cp.getOnlinetime(),
                "§7Coins: §f" + cp.getCoins(),
                "§7Status: " + cp.getState().getName(),
                "§7Herzen: §c" + replayPlayer.getHealth(),
                "§7Reportet: " + (replayPlayer.getData().isReported() ? "§cJa: " : "§aNein"),
                "",
                "§7Freundschaftsanfrage senden? §8(§7Klick§8)"
        ).getItemStack(), e -> CoreSystem.getInstance().getChannelHandler().createSetRequest(player, "CMD", "friend add " + replayPlayer.getData().getName()));

        setItem(InventorySlot.ROW_3_SLOT_4, new ItemBuilder(Material.DIAMOND_SWORD, 1).displayName("§cStats").lore(
                "§7Kills: §f" + replayPlayer.getStats().getKills(),
                "§7Tode: §f" + replayPlayer.getStats().getDeaths(),
                "§7Goals: §f" + replayPlayer.getStats().getGoals(),
                "§7K/D: §f" + (replayPlayer.getStats().getKills() != 0 || replayPlayer.getStats().getDeaths() != 0 ? ((replayPlayer.getStats().getKills() / replayPlayer.getStats().getDeaths() > 0) ? (replayPlayer.getStats().getKills() / replayPlayer.getStats().getDeaths()) : 0) : 0)
        ).create());

        setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1).displayName("§7Rüstung").create(), e -> new ReplayPlayerArmorInventory(replayPlayer, player));
        setItem(InventorySlot.ROW_3_SLOT_6, new ItemBuilder(Material.CHEST, 1).displayName("§eInventar").create(), e -> replayPlayer.openInventory(player));

        openInventory();
    }
}
