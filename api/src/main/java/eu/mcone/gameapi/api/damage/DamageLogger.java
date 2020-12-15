/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.damage;

import org.bukkit.entity.Player;

public interface DamageLogger {

    int getDamageCooldown();

    void setDamageCooldown(int cooldown);

    Player getKiller(Player p);
}
