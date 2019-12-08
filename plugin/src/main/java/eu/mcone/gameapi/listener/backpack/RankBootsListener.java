package eu.mcone.gameapi.listener.backpack;

import eu.mcone.gameapi.backpack.GameBackpackManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class RankBootsListener implements Listener {

    private final GameBackpackManager manager;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (manager.isUseRankBoots()) {
            manager.setRankBoots(e.getPlayer());
        }
    }

}
