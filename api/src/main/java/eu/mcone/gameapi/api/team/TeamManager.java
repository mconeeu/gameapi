package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface TeamManager {

    ItemStack TEAM = new ItemBuilder(Material.BED, 1, 0).displayName("§c§lTeam §8» §7§owähle dein Team aus.").create();

    void loadDefaultTeams();

    Team registerNewTeam(String name, String label, int priority, ChatColor color, ItemStack item);

    Team createTeam(String name, String label, int priority, ChatColor color, ItemStack item);

    Team getTeam(String name);

    void stopGameWithWinner(Team team);

    void setTeamsForRemainingPlayersBalanced();

    void setTeamsForRemainigPlayersByPriority();

    Team getWinnerTeamIfLastSurvived();

    Team calculateWinnerByGoals();

    Team calculateWinnerByKills();

    void sendKillMessage(Player receiver, Player victim, Player killer);

    List<Team> getTeams();

    Collection<Team> getAliveTeams();

    TeamChatListener getTeamChatListener();

    void setTeamChatListener(TeamChatListener chatListener);

    void openTeamInventory(Player p);

    boolean isDisableWinMethod();
}
