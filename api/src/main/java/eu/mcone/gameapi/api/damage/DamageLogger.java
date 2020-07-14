package eu.mcone.gameapi.api.damage;

import org.bukkit.entity.Player;

public interface DamageLogger {

    int getDamageCooldown();

    void setDamageCooldown(int cooldown);

    Player getKiller(Player p);
}
