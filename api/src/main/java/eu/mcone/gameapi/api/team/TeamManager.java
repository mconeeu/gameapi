/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface TeamManager {

    void loadDefaultTeams();

    Team registerNewTeam(String name, String label, int priority, ChatColor color, ItemStack item);

    Team createTeam(String name, String label, int priority, ChatColor color, ItemStack item);

    Team getTeam(String name);

    Team getWinner();

    void stopGameWithWinner(Team team);

    void setTeamsForRemainingPlayersBalanced();

    void setTeamsForRemainigPlayersByPriority();

    Team getWinnerTeamIfLastSurvived();

    Team calculateWinnerByGoals();

    Team calculateWinnerByKills();

    List<Team> getTeams();

    Collection<Team> getAliveTeams();

    TeamChatListener getTeamChatListener();

    void setTeamChatListener(TeamChatListener chatListener);

    void openTeamInventory(Player p);

    boolean isTeamsFinallySet();

    boolean isDisableWinMethod();
}
