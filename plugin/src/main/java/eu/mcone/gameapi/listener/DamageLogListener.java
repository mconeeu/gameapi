package eu.mcone.gameapi.listener;

import eu.mcone.gameapi.api.event.PlayerKilledByPlayerEvent;
import eu.mcone.gameapi.damage.GameDamageLogger;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
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
            Player p = (Player) e.getEntity();

            if (e.getDamager() instanceof Player) {
                damageLogger.logDamage(p, (Player) e.getDamager());
            } else if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
                damageLogger.logDamage(p, (Player) ((Arrow) e.getDamager()).getShooter());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();

        if (killer == null) {
            killer = damageLogger.getKiller(p);
        }

        if (killer != null) {
            PlayerKilledByPlayerEvent event = new PlayerKilledByPlayerEvent(
                    e.getEntity(), killer, e.getDrops(), e.getDroppedExp(), e.getNewExp(), e.getNewTotalExp(), e.getNewLevel(), e.getKeepLevel(), e.getKeepInventory(), e.getDeathMessage()
            );

            damageLogger.getPlugin().getServer().getPluginManager().callEvent(event);

            e.setDeathMessage(event.getDeathMessage());
            e.setKeepInventory(event.isKeepInventory());
            e.setKeepLevel(event.isKeepLevel());
            e.setNewExp(event.getNewExp());
            e.setNewLevel(event.getNewLevel());
            e.setNewTotalExp(event.getNewTotalExp());
            e.setDroppedExp(event.getDropExp());
        }
    }

}
