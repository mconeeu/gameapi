/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack.handler;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import org.bukkit.entity.Player;

public interface OutfitHandler {

    void setOutfit(Player p, BackpackItem item);

    void setOutfit(Player p, DefaultItem item);
}
