/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.damage;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.listener.DamageLogListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameDamageLogger implements DamageLogger {

    @Getter
    @Setter
    private int damageCooldown = 8;

    @Getter
    private final GameAPIPlugin plugin;
    private final Map<UUID, Map<UUID, Long>> damageLog;

    public GameDamageLogger(GameAPIPlugin system) {
        this.plugin = system;
        this.damageLog = new HashMap<>();

        system.sendConsoleMessage("§aLoading DamageLogger...");
        system.registerEvents(new DamageLogListener(this));
    }

    public void logDamage(Player player, Player damager) {
        if (damageLog.containsKey(player.getUniqueId())) {
            damageLog.get(player.getUniqueId()).put(damager.getUniqueId(), System.currentTimeMillis() / 1000);
        } else {
            damageLog.put(player.getUniqueId(), new HashMap<UUID, Long>() {{
                put(damager.getUniqueId(), System.currentTimeMillis() / 1000);
            }});
        }
    }

    @Override
    public Player getKiller(Player p) {
        if (damageLog.containsKey(p.getUniqueId()) && damageLog.get(p.getUniqueId()).size() > 0) {
            HashMap.Entry<UUID, Long> entry = null;
            for (HashMap.Entry<UUID, Long> e : damageLog.get(p.getUniqueId()).entrySet()) {
                entry = e;
            }

            if ((entry != null) && (entry.getValue() > (System.currentTimeMillis() / 1000) - damageCooldown)) {
                return Bukkit.getPlayer(entry.getKey());
            }
        }

        return null;
    }

    public void playerDied(Player p) {
        damageLog.remove(p.getUniqueId());
    }

}
