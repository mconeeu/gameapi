package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.gameapi.api.Option;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public interface TeamManager {

    ItemStack TEAM = new ItemBuilder(Material.BED, 1, 0).displayName("§c§lTeam §8» §7§owähle dein Team aus.").create();

    List<Option> getOptions();

    PlayingChat getPlayingChat();

    Class<? extends CoreScoreboard> getTeamTablist();

    boolean isExitBySingleDeath();

    boolean isUseCustomTeams();

    void addCustomTeam(Team team);

    void addTeamTablist(Class<? extends CoreScoreboard> scoreboard);

    void addTeamChat(PlayingChat teamChat);

    Team getTeam(final String team);

    Team getWonTeam();

    boolean hasPlayerTeam(final Player player);

    void setupTeam();

    void setupTeamWherePriority();

    Team checkChanceToWin();

    Team getPrematureWinner();

    void sendKillMessage(Player receiver, final Player victim, final Player killer);

    Collection<Team> getTeams();

    Team getTeam(int index);

    Collection<Team> getAliveTeams();

    LinkedList<Team> sortTeamsWherePriority();

    LinkedList<Team> sortTeamsWhereSize();

    void openTeamInventory(Player p);
}
