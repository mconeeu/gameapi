package eu.mcone.gameapi.api.replay.player;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.objectives.ReplayPlayerSidebarObjective;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@BsonDiscriminator
public interface ReplayPlayer {

    UUID getUuid();

    String getName();

    String getDisplayName();

    boolean isReported();

    CoreLocation getSpawnLocation();

    void setReported(boolean reported);

    long getJoined();

    long getLeaved();

    void setJoined(long joined);

    void setLeaved(long leaved);

    void setSpawnLocation(CoreLocation location);

    Map<Player, CoreInventory> getInventoryViewers();

    void setInventoryItems(Map<Integer, ItemStack> items);

    PlayerNpc getNpc();

    void setNpc(PlayerNpc npc);

    void setInventoryItem(int slot, ItemStack itemStack);

    void openInventory(ReplayContainer container, Player player);

    Stats getStats();

    int getHealth();

    void setHealth(int health);

    int getFood();

    void setFood(int food);

    ReplayPlayerSidebarObjective getScoreboard();

    void setScoreboard(ReplayPlayerSidebarObjective objective);

    interface Stats {
        int getKills();

        void setKills(int kills);

        int getDeaths();

        void setDeaths(int deaths);

        int getGoals();

        void setGoals(int goals);
    }
}
