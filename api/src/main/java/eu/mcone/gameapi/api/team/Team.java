package eu.mcone.gameapi.api.team;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Team {

    @Getter
    private final Teams teamEnum;
    @Getter
    private int size;
    @Getter
    private boolean alive;
    @Getter
    private String spawnLocation;
    @Getter
    private String npcLocation;
    @Getter
    private String respawnBlockLocation;

    private List<Player> players;

    public Team(final Teams teamEnum, final String spawnLocation, final String npcLocation, final String respawnBlockLocation) {
        this.teamEnum = teamEnum;
        this.spawnLocation = spawnLocation;
        this.npcLocation = npcLocation;
        this.respawnBlockLocation = respawnBlockLocation;

        this.players = new ArrayList<>();
    }

    public void addPlayer(final Player player) {
        if (!players.contains(player)) {
            players.add(player);
            size += 1;
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
    }

    public boolean containsPlayer(final Player player) {
        return players.contains(player);
    }

    public Collection<Player> getPlayers() {
        return players;
    }
}
