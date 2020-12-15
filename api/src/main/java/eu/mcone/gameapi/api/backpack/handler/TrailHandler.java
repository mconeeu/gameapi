/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack.handler;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import org.bukkit.entity.Player;

public interface TrailHandler {
    void setTrail(Player p, BackpackItem trail);

    void removeTrail(Player p);

    void stop();
}
