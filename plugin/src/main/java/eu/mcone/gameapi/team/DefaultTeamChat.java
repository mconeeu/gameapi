package eu.mcone.gameapi.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.TeamChatListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultTeamChat extends TeamChatListener {

    @Override
    public void onPlayingChat(String message, Player p, GamePlayer gp) {
        if (message.startsWith("@all")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("§8[§b@all§8] " + gp.getTeam().getColor() + p.getName() + " §8»§7 " + message.replace("@all", ""));
            }
        } else {
            for (GamePlayer player : gp.getTeam().getPlayers()) {
                player.bukkit().sendMessage(gp.getTeam().getColor() + p.getName() + " §8»§7 " + message);
            }
        }
    }

}
