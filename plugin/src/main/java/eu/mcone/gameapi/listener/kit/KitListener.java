package eu.mcone.gameapi.listener.kit;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.kit.GameKitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

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

                manager.setKit(manager.getDefaultKit(), p);
            }, 20);
        }
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        if (manager.getDefaultKit() != null) {
            Bukkit.getScheduler().runTaskLater(GameAPIPlugin.getSystem(), () -> manager.setKit(manager.getDefaultKit(), e.getPlayer()), 20);
        }
    }
}
