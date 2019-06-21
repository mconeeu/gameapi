/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.player;

import eu.mcone.gamesystem.api.player.IDamageLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DamageLogger implements IDamageLogger {

    @Getter
    private Map<UUID, Map<UUID, Long>> players;

    public DamageLogger() {
        this.players = new LinkedHashMap<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            players.put(p.getUniqueId(), new LinkedHashMap<>());
        }
    }

    public void logDamage(Player damaged, Player damager) {
        players.get(damaged.getUniqueId()).put(damager.getUniqueId(), System.currentTimeMillis() / 1000);
    }

    public Player getKiller(Player p) {
        if (players.get(p.getUniqueId()).size() > 0) {
            HashMap.Entry<UUID, Long> entry = null;
            for (HashMap.Entry<UUID, Long> e : players.get(p.getUniqueId()).entrySet()) {
                entry = e;
            }

            if ((entry != null) && (entry.getValue() > (System.currentTimeMillis() / 1000) - 5)) {
                return Bukkit.getPlayer(entry.getKey());
            }
        }
        return null;
    }

}
