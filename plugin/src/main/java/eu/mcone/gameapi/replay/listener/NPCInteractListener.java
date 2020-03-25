package eu.mcone.gameapi.replay.listener;

import eu.mcone.coresystem.api.bukkit.event.npc.NpcInteractEvent;
import eu.mcone.coresystem.api.bukkit.npc.NPC;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInteractInventory;
import eu.mcone.gameapi.replay.runner.ReplayRunnerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class NPCInteractListener implements Listener {

    private ReplayRunnerManager manager;
    @Setter
    @Getter
    private boolean unregister = false;

    public NPCInteractListener(ReplayRunnerManager manager) {
        this.manager = manager;
        GamePlugin.getGamePlugin().registerEvents(this);
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player player = (Player) e.getWhoClicked();
        if (unregister) {
            e.getHandlers().unregister(this);
        } else {
            if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                for (ReplayPlayer replayPlayer : manager.getSession().getPlayers()) {
                    if (replayPlayer.getNpc().getData().getName().equalsIgnoreCase(inv.getName())) {
                        if (replayPlayer.getInventoryViewers().containsKey(player)) {
                            e.setCancelled(true);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(NpcInteractEvent e) {
        NPC npc = e.getNpc();

        if (unregister) {
            e.getHandlers().unregister(this);
        } else {
            if (npc instanceof PlayerNpc) {
                for (ReplayPlayer replayPlayer : manager.getSession().getPlayers()) {
                    System.out.println(replayPlayer.getData().getName());
                    if (replayPlayer.getNpc().equals(npc)) {
                        new ReplayPlayerInteractInventory(replayPlayer, e.getPlayer());
                        break;
                    }
                }
            }
        }
    }
}
