package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.event.team.TeamDestroyEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Team {

    @Getter
    private final String name;
    @Getter
    private final String prefix;
    @Getter
    @Setter
    private int size;
    @Getter
    private final int priority;
    @Getter
    private boolean alive = false;

    @Getter
    private final ChatColor chatColor;
    @Getter
    private final Color color;
    @Getter
    private final ItemStack item;

    @Getter
    @Setter
    private String spawnLocation;
    @Getter
    private final String npcLocation;
    @Getter
    private final String respawnBlockLocation;

    private final List<Player> players;

    public Team(final TeamDefinition teamDefinition) {
        this(teamDefinition.getName(), 0, teamDefinition.getPrefix(), teamDefinition.getChatColor(), teamDefinition.getColor(), teamDefinition.getItemStack());
    }

    public Team(final String team, final int priority, final String prefix, ChatColor chatColor, Color color, ItemStack item) {
        this.name = team;
        this.priority = priority;
        this.prefix = prefix;
        this.size = GamePlugin.getGamePlugin().getGameConfig().parseConfig().getPlayersPerTeam();
        this.chatColor = chatColor;
        this.color = color;
        this.item = item;
        this.spawnLocation = team + ".spawn";
        this.npcLocation = team + ".npc";
        this.respawnBlockLocation = team + ".respawn";
        this.players = new ArrayList<>();
    }

    public void addPlayer(final Player player) {
        if (!players.contains(player)) {
            if (players.size() < size) {
                players.add(player);
                alive = true;
            }
        }
    }

    public void removePlayer(final Player player) {
        players.remove(player);
    }

    public void setAlive(boolean var) {
        alive = var;

        if (!var)
            Bukkit.getPluginManager().callEvent(new TeamDestroyEvent(this));
    }

    public boolean containsPlayer(final Player player) {
        return players.contains(player);
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public TeamDefinition getDefinition() {
        for (TeamDefinition teamDefinition : TeamDefinition.values()) {
            if (teamDefinition.getName().equalsIgnoreCase(name)) {
                return teamDefinition;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", size=" + size +
                ", priority=" + priority +
                ", alive=" + alive +
                ", chatColor=" + chatColor +
                ", color=" + color +
                ", item=" + item +
                ", spawnLocation='" + spawnLocation + '\'' +
                ", npcLocation='" + npcLocation + '\'' +
                ", respawnBlockLocation='" + respawnBlockLocation + '\'' +
                ", players=" + players +
                '}';
    }
}
