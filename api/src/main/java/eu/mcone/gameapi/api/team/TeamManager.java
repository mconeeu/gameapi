package eu.mcone.gameapi.api.team;

import org.bukkit.entity.Player;

public interface TeamManager {

    Team getTeam(final TeamEnum teamEnum);

    boolean hasPlayerTeam(final Player player);
}
