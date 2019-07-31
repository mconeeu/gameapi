/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game.manager.team;

import eu.mcone.gamesystem.api.game.Team;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ITeamManager {

    /**
     * Returns the teamStage
     * @return TeamStage interface
     */
    ITeamStageHandler getTeamStageHandler();

    /**
     * Returns the team selection item for the player inventory
     * @return ItemStack
     */
    ItemStack getTeamSelectionItem();

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
    boolean checkChanceToWin();

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
