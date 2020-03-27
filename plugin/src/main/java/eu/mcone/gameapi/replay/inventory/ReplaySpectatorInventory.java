package eu.mcone.gameapi.replay.inventory;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.session.ReplaySession;
import org.bukkit.entity.Player;

public class ReplaySpectatorInventory extends CoreInventory {

    public ReplaySpectatorInventory(ReplaySession session, Player p) {
        super("§eZuschauen", p, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);

        int slot = 0;
        for (ReplayPlayer rPlayer : session.getPlayers()) {
            setItem(slot, new Skull(rPlayer.getData().getName()).setDisplayName(rPlayer.getData().getDisplayName()).getItemStack(), e -> {
                p.teleport((rPlayer.getNpc() != null ? rPlayer.getNpc().getLocation() : rPlayer.getData().getSpawnLocation().bukkit()));
                GamePlugin.getGamePlugin().getMessager().send(p, "§aDu wurdest zum Spieler §f" + rPlayer.getData().getName() + " §ateleportiert!");
            });
            slot++;
        }

        openInventory();
    }
}
