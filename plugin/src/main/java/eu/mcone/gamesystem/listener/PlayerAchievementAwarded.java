/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class PlayerAchievementAwarded implements Listener{

    @EventHandler
    public void on(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

}
