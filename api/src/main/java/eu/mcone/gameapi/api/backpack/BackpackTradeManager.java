/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack;

import org.bukkit.entity.Player;

public interface BackpackTradeManager {
    boolean hasTraidingRequest(Player player, Player target);

    Player getTraidingPartner(Player p);

    void cancelTraid(Player player);

    void makeTraidRequest(Player p, Player target) throws IllegalArgumentException;

    void openBackpackTraidInventory(Player player);
}
