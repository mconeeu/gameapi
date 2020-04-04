package eu.mcone.gameapi.api.replay.chunk;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReplayChunk {

    void addServerPacket(int tick, PacketWrapper wrapper);

    void addPacket(UUID uuid, int tick, PacketWrapper wrapper);

    byte[] compressData();

    Map<Integer, List<PacketWrapper>> getPackets(UUID uuid);

    List<PacketWrapper> getServerPackets(Integer tick);

    PacketWrapper getLastPacketInRange(UUID uuid, Object obj, int startTick, int endTick);

    Collection<UUID> getPlayers();

    public interface ChunkData {

        Map<UUID, Map<Integer, List<PacketWrapper>>> getPlayerPackets();

        Map<Integer, List<PacketWrapper>> getServerPackets();

        int getPacketsAmount();
    }
}
