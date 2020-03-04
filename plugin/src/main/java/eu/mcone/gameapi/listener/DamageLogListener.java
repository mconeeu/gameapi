package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.damage.GameDamageLogger;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@RequiredArgsConstructor
public class DamageLogListener implements Listener {

    private final GameDamageLogger damageLogger;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                damageLogger.logDamage(p, (Player) e.getDamager());
            } else if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
                damageLogger.logDamage(p, (Player) ((Arrow) e.getDamager()).getShooter());
            }
        }
    }

}
