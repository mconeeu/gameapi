package eu.mcone.gameapi.api.replay.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gameapi.api.replay.utils.Replay;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ReplayPlayer {

    UUID getUuid();

    Data getData();

    ItemStack[] getInventoryContent();

    ReplayPlayer.Stats getStats();

    double getHealth();

    Replay getReplay();

    HashMap<String, List<PacketWrapper>> getPackets();

    void addPacket(final int tick, final PacketWrapper packet);

    interface Data {
        String getDisplayName();

        String getName();

        boolean isReported();

        SkinInfo getSkinInfo();

        void setJoined(long joined);
    }

    interface Stats {
        int getKills();

        int getDeaths();

        int getGoals();
    }
}
