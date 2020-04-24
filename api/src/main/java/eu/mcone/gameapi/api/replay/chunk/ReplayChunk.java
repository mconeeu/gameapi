package eu.mcone.gameapi.api.replay.chunk;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReplayChunk {

    void addServerPacket(int tick, PacketContainer wrapper);

    void addPacket(UUID uuid, int tick, PacketContainer wrapper);

    byte[] compressData();

    Map<Integer, List<PacketContainer>> getPackets(UUID uuid);

    List<PacketContainer> getServerPackets(Integer tick);

    PacketContainer getLastPacketInRange(UUID uuid, Object obj, int startTick, int endTick);

    Collection<UUID> getPlayers();

    interface ChunkData {

        Map<UUID, Map<Integer, List<PacketContainer>>> getPlayerPackets();

        Map<Integer, List<PacketContainer>> getServerPackets();

        int getPacketsAmount();
    }
}
