package eu.mcone.gameapi.api.damage;

import org.bukkit.entity.Player;

public interface DamageLogger {
    Player getKiller(Player p);
}
