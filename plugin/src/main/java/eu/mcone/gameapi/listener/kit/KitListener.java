package eu.mcone.gameapi.listener.kit;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.kit.Kit;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
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

        final Kit kit;
        if (gp.getCurrentKit() == null && manager.getDefaultKit() != null) {
            kit = manager.getDefaultKit();

            GamePlugin.getGamePlugin().getMessenger().sendInfo(e.getBukkitPlayer(), "Du scheinst neu auf KnockIT zu sein! Du bekommst das ![Standart-Kit]!");
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
        } else if (gp.getCurrentKit() != null && GamePlugin.getGamePlugin().hasOption(Option.KIT_MANAGER_SET_CURRENT_KIT_ON_JOIN)) {
            kit = gp.getCurrentKit();
        } else {
            kit = null;
        }

        if (kit != null) {
            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> {
                gp.setKit(kit, true);
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
                        ((GameAPIPlayer) gp).buyKit(gp.getCurrentKit(), true);
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
                if (inv.getTitle().equals("§8» §c§lKits §8| §7Sortieren") && e.getRawSlot() < 9) {
                    e.setCancelled(false);
                }
            }
        }
    }

}
