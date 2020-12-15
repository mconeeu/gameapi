/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public abstract class TeamChatListener {

    public abstract void onPlayingChat(String message, Player player, GamePlayer gamePlayer);
}
