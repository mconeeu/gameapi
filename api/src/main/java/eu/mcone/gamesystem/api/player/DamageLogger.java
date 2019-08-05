/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.player;

import org.bukkit.entity.Player;

public interface DamageLogger {

    /**
     * Logs the last hit of the player
     *
     * @param damaged Attacked player
     * @param damager Attacker
     */
    void logDamage(Player damaged, Player damager);

    /**
     * Returns the player who was last hit
     *
     * @param p Specifies the target player
     * @return Returns the player who last hit the target player
     */
    Player getKiller(Player p);

}
