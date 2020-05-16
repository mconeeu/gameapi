package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public abstract class TeamChatListener {

    public abstract void onPlayingChat(String message, Player player, GamePlayer gamePlayer);
}
