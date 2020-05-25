package eu.mcone.gameapi.api.team;

import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Team {

    String getName();

    String getLabel();

    int getSize();

    void setSize(int size);

    int getPriority();

    boolean isAlive();

    ChatColor getColor();

    ItemStack getItem();

    String getSpawnLocation();

    void setSpawnLocation(String spawnLocation);

    String getNpcLocation();

    void setNpcLocation(String npcLocation);

    String getRespawnBlockLocation();

    void setRespawnBlockLocation(String respawnBlockLocation);

    List<GamePlayer> getPlayers();

}
