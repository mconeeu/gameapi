package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.entity.Player;

public abstract class TeamChat {

    public abstract void onTeamChat(String message, Player player, GamePlayer gamePlayer);
}
