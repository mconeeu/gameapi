/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.gamesystem.GameSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class PlayerAchievementAwarded implements Listener{

    public PlayerAchievementAwarded() {
        Bukkit.getServer().getPluginManager().registerEvents(this, GameSystem.getInstance());
    }

    @EventHandler
    public void on(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

}
