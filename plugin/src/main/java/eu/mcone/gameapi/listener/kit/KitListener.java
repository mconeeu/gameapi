package eu.mcone.gameapi.listener.kit;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class KitListener implements Listener {

    private final GameKitManager manager;

    @EventHandler
    public void on(GamePlayerLoadedEvent e) {
        Player p = e.getBukkitPlayer();
        GamePlayer gp = e.getPlayer();

        if (gp.getCurrentKit() == null && manager.getDefaultKit() != null) {
            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);

                gp.setKit(manager.getDefaultKit());
            }, 20);
        }
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        if (manager.getDefaultKit() != null) {
            Player p = e.getPlayer();

            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                if (p.isOnline()) {
                    GamePlayer gp = GameAPI.getInstance().getGamePlayer(p);

                    if (gp.isAutoBuyKit()) {
                        gp.buyKit(gp.getCurrentKit());
                    } else {
                        manager.setKit(manager.getDefaultKit(), p);
                    }
                }
            }, 20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Inventory inv = e.getClickedInventory();

            if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && e.getRawSlot() < inv.getSize() && e.getCurrentItem() != null) {
                if (inv.getTitle().endsWith(" §8| §7Kit sortieren") && e.getRawSlot() < 9) {
                    e.setCancelled(false);
                }
            }
        }
    }

}
