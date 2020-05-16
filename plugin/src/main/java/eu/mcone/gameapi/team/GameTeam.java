package eu.mcone.gameapi.team;

import eu.mcone.gameapi.api.event.team.TeamDestroyEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.DefaultTeam;
import eu.mcone.gameapi.api.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameTeam implements Team {

    private final String name, label;
    @Setter
    private int size;
    private final int priority;
    private boolean alive;
    private final ChatColor color;
    private final ItemStack item;
    private final String spawnLocation, npcLocation, respawnBlockLocation;
    private final List<GamePlayer> players;

    public GameTeam(String name, String label, int priority, ChatColor color, ItemStack item) {
        this.name = name;
        this.label = label;
        this.priority = priority;
        this.color = color;
        this.item = item;

        this.spawnLocation = "team." + name.toLowerCase() + ".spawn";
        this.npcLocation = "team." + name.toLowerCase() + ".npc";
        this.respawnBlockLocation = "team." + name.toLowerCase() + ".respawn";
        this.players = new ArrayList<>();
    }

    public void addPlayer(final GamePlayer player) {
        if (!players.contains(player)) {
            if (players.size() < size) {
                players.add(player);
                alive = true;
            }
        }
    }

    public void removePlayer(final GamePlayer player) {
        players.remove(player);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;

        if (!alive)
            Bukkit.getPluginManager().callEvent(new TeamDestroyEvent(this));
    }

    public boolean hasPlayer(final GamePlayer player) {
        return players.contains(player);
    }

    public DefaultTeam getDefaultTeam() {
        for (DefaultTeam team : DefaultTeam.values()) {
            if (team.getTeam().equals(this)) {
                return team;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "GameTeam{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", size=" + size +
                ", priority=" + priority +
                ", alive=" + alive +
                ", color=" + color +
                ", item=" + item +
                ", spawnLocation='" + spawnLocation + '\'' +
                ", npcLocation='" + npcLocation + '\'' +
                ", respawnBlockLocation='" + respawnBlockLocation + '\'' +
                ", players=" + players +
                '}';
    }

}
