package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.event.team.TeamDestroyEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Team {

    @Getter
    private final TeamDefinition team;
    @Getter
    private int size;
    @Getter
    private boolean alive = false;
    @Getter
    private String spawnLocation;
    @Getter
    private String npcLocation;
    @Getter
    private String respawnBlockLocation;

    private List<Player> players;

    public Team(final TeamDefinition teamDefinition) {
        this.team = teamDefinition;
        this.spawnLocation = team.getTeam() + ".spawn";
        this.npcLocation = team.getTeam() + ".npc";
        this.respawnBlockLocation = team.getTeam() + ".respawn";
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
}
