package eu.mcone.gameapi.team;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.TeamChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultTeamChat extends TeamChat {

    @Override
    public void onTeamChat(String message, Player player, GamePlayer gamePlayer) {
        if (message.startsWith("@all")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                GamePlugin.getGamePlugin().getMessenger().sendSimple(all, "§8[§b@all§8] " + gamePlayer.getTeam().getChatColor().toString() + player.getName() + " §8»§7 " + message.replace("@all", ""));
            }
        } else {
            for (Player team : gamePlayer.getTeam().getPlayers()) {
                GamePlugin.getGamePlugin().getMessenger().sendSimple(team, gamePlayer.getTeam().getChatColor().toString() + player.getName() + " §8»§7 " + message);
            }
        }
    }
}
