package eu.mcone.gameapi.api.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.Option;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface TeamManager {

    ItemStack TEAM = new ItemBuilder(Material.BED, 1, 0).displayName("§c§lTeam §8» §7§owähle dein Team aus.").create();

    List<Option> getOptions();

    boolean isExitBySingleDeath();

    void useTeamChat(boolean use);

    Team getTeam(final TeamDefinition team);

    Team getWonTeam();

    boolean hasPlayerTeam(final Player player);

    void setupTeam();

    Team checkChanceToWin();

    Team getPrematureWinner();

    void sendKillMessage(Player receiver, final Player victim, final Player killer);

    Collection<Team> getTeams();

    Team getTeam(int index);

    Collection<Team> getAliveTeams();

    void openTeamInventory(Player p);
}
