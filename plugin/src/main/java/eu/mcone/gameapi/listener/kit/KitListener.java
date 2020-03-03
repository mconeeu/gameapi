package eu.mcone.gameapi.listener.kit;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.kit.GameKitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class KitListener implements Listener {

    private final GameKitManager manager;

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (manager.getCurrentKit(p) == null && manager.getDefaultKit() != null) {
            manager.setKit(manager.getDefaultKit(), p);
        }
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        if (manager.getDefaultKit() != null) {
            Bukkit.getScheduler().runTask(GameAPIPlugin.getSystem(), () -> manager.setKit(manager.getDefaultKit(), e.getPlayer()));
        }
    }

}
