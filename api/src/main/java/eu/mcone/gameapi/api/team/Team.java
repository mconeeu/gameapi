package eu.mcone.gameapi.api.team;

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
    private int size;
    @Getter
    private boolean alive = false;

    @Getter
    private ChatColor chatColor;
    @Getter
    private Color color;
    @Getter
    private ItemStack item;

    @Getter
    @Setter
    private String spawnLocation;
    @Getter
    private final String npcLocation;
    @Getter
    private final String respawnBlockLocation;

    private final List<Player> players;

    public Team(final TeamDefinition teamDefinition) {
        this.name = teamDefinition.getName();
        this.prefix = teamDefinition.getPrefix();
        this.spawnLocation = name + ".spawn";
        this.npcLocation = name + ".npc";
        this.respawnBlockLocation = name + ".respawn";
        this.players = new ArrayList<>();
    }

    public Team(final String team, final String prefix, ChatColor chatColor, Color color, ItemStack item) {
        this.name = team;
        this.prefix = prefix;
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
            players.add(player);
            size += 1;
            alive = true;
        }
    }

    public void removePlayer(final Player player) {
        if (players.contains(player)) {
            players.remove(player);
            size -= 1;
        }
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
}
