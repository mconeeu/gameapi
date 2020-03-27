package eu.mcone.gameapi.api.team;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface TeamManager {

    Team getTeam(final Teams team);

    boolean hasPlayerTeam(final Player player);

    void setupTeam();

    boolean checkChanceToWin();

    void sendKillMessage(Player receiver, final Player victim, final Player killer);

    Collection<Team> getTeams();

    void createTeamInventory(Player p);
}
