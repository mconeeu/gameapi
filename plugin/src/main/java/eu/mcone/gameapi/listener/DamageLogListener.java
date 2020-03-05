package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.damage.GameDamageLogger;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

@RequiredArgsConstructor
public class DamageLogListener implements Listener {

    private final GameDamageLogger damageLogger;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player t = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                damageLogger.logDamage(t, (Player) e.getDamager());
            } else if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player) {
                Player p = (Player) ((Projectile) e.getDamager()).getShooter();

                if (!p.equals(t)) {
                    damageLogger.logDamage(t, p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        damageLogger.playerDied(e.getEntity());
    }

}
