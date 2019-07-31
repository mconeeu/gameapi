package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void on(EntityDamageEvent e) {
        if (GameTemplate.getInstance() != null) {
            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_GAME_STATE_HANDLER)) {
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                        e.setCancelled(true);
                        CoreSystem.getInstance().getWorldManager().getWorld(p.getWorld()).teleport(p, "spawn");
                        p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
