/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.player;

import eu.mcone.gamesystem.GameSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DamageLogger implements Listener, eu.mcone.gamesystem.api.player.DamageLogger {

    private Map<UUID, Map<UUID, Long>> players;

    public DamageLogger() {
        this.players = new LinkedHashMap<>();
        Bukkit.getPluginManager().registerEvents(this,  GameSystem.getInstance());

        for (Player p : Bukkit.getOnlinePlayers()) {
            players.put(p.getUniqueId(), new LinkedHashMap<>());
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        players.put(e.getPlayer().getUniqueId(), new HashMap<>());
    }

    public void logDamage(Player damaged, Player damager) {
        players.get(damaged.getUniqueId()).put(damager.getUniqueId(), System.currentTimeMillis()/1000);
    }

    public Player getKiller(Player p) {
        if (players.get(p.getUniqueId()).size() > 0) {
            HashMap.Entry<UUID, Long> entry = null;
            for (HashMap.Entry<UUID, Long> e : players.get(p.getUniqueId()).entrySet()) {
                entry = e;
            }

            if ((entry != null) && (entry.getValue() > (System.currentTimeMillis() / 1000) - 5)) {
                Player k = Bukkit.getPlayer(entry.getKey());

                if (k != null) {
                    return k;
                }
            }
        }
        return null;
    }

}