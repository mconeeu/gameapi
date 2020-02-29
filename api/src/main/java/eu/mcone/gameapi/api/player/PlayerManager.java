package eu.mcone.gameapi.api.player;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerManager {

    List<Player> getPlaying();

    int getMinPlayers();

    int getMaxPlayers();

    void addSpectator(final Player player);

    void removeSpectator(final Player player);

    boolean isSpectator(final Player player);

    void setSpectating(final Player player, final boolean var);

    void setPlaying(final Player player, final boolean var);

    void openSpectatorInventory(final Player player);
}
