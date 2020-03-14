package eu.mcone.gameapi.api.replay.player;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface ReplayPlayer {

    UUID getUuid();

    Data getData();

    Map<Player, CoreInventory> getInventoryViewers();

    void setInventoryItems(Map<Integer, SerializableItemStack> items);

    PlayerNpc getNpc();

    void openInventory(Player player);

    ReplayPlayer.Stats getStats();

    void setStats(ReplayPlayer.Stats stats);

    double getHealth();

    void setHealth(double health);

    interface Data {

        String getDisplayName();

        String getName();

        boolean isReported();

        void setReported(boolean reported);

        void setJoined(long joined);

        void setWorld(String world);

        String getWorld();
    }

    interface Stats {
        int getKills();

        int getDeaths();

        int getGoals();
    }
}
