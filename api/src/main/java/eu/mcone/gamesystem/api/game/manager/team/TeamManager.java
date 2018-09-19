package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.coresystem.api.core.player.GlobalCorePlayer;
import eu.mcone.gamesystem.api.game.Team;
import org.bukkit.entity.Player;

public interface TeamManager {

    /**
     * Returns the team where the team name
     * @param name Team name
     * @return Team enum
     */
    Team getTeam(String name);

    /**
     * Put the players in a team that did not select one
     */
    void setupTeam();

    /**
     * Check if there is already a winner of the current round
     */
    void chekWinntection();

    /**
     * Returns the death message as a string
     * @param receiver GlobalCorePlayer
     * @param killer Player
     * @param victim Player
     */

    void sendKillMessage(Player receiver, final Player victim, final Player killer);

    /**
     * Create a new TeamInventory
     * @param p
     */
    void createTeamInventory(Player p);
}
