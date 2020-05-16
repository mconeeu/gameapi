package eu.mcone.gameapi.listener.team;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.team.GameTeamManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@RequiredArgsConstructor
public class TeamListener implements Listener {

    private final GameTeamManager manager;

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerDeathEvent e) {
        if (manager.isDisableRespawn()) {
            GameAPI.getInstance().getGamePlayer(e.getEntity()).removeFromGame();
        }
    }

}
