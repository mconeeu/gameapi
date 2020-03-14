package eu.mcone.gameapi.replay.listener;

import eu.mcone.coresystem.api.bukkit.event.npc.NpcInteractEvent;
import eu.mcone.coresystem.api.bukkit.npc.NPC;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInteractInventory;
import eu.mcone.gameapi.replay.session.ReplaySession;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class NPCInteractListener implements Listener {

    private ReplaySession session;
    @Setter
    @Getter
    private boolean unregister = false;

    public NPCInteractListener(ReplaySession session) {
        this.session = session;
        GamePlugin.getGamePlugin().registerEvents(this);
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
            for (ReplayPlayer replayPlayer : session.getPlayers()) {
                if (replayPlayer.getNpc().getData().getName().equalsIgnoreCase(inv.getName())) {
                    if (replayPlayer.getInventoryViewers().containsKey(player)) {
                        e.setCancelled(true);
                    }

                    break;
                }
            }
        }

        if (unregister) {
            e.getHandlers().unregister(this);
        }
    }

    @EventHandler
    public void on(NpcInteractEvent e) {
        NPC npc = e.getNpc();

        if (npc instanceof PlayerNpc) {
            for (ReplayPlayer replayPlayer : session.getPlayers()) {
                if (replayPlayer.getNpc().equals(npc)) {
                    new ReplayPlayerInteractInventory(replayPlayer, e.getPlayer());
                    break;
                }
            }
        }

        if (unregister) {
            e.getHandlers().unregister(this);
        }
    }
}
