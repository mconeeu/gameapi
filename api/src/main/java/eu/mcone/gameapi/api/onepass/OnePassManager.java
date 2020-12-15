/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.onepass;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public interface OnePassManager {

    int ONE_PASS_PREMIUM_LEVEL = 10, NEEDED_XP_FOR_NEXT_LEVEL = 10, BUY_NEW_LEVEL_PRICE = 15, MAX_ONE_PASS_LEVEL = 24;

    void levelChanged(GamePlayer gp, int oldLevel, int newLevel, boolean notify);

    void openOnePassInventory(Player player);

    int getSecretAward();
}
