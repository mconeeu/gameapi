package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.team.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class GeneralInteractListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerInteractEvent e) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            if (e.getItem() != null) {
                Player player = e.getPlayer();

                if (e.getItem().equals(PlayerManager.SPECTATOR)) {
                    GamePlugin.getGamePlugin().getPlayerManager().openSpectatorInventory(player);
                } else if (e.getItem().equals(TeamManager.TEAM)) {
                    GamePlugin.getGamePlugin().getTeamManager().openTeamInventory(player);
                } else if (e.getItem().equals(DefaultItem.GRAPPLING_HOOK.getItemStack())
                        || e.getItem().equals(DefaultItem.ENDERPEARL.getItemStack())) {
                    e.setCancelled(false);
                }
            }
        }
    }
}
