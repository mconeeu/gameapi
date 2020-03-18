package eu.mcone.gameapi.api.replay.chunk;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReplayChunk {

    void addPacket(UUID uuid, int tick, PacketWrapper wrapper);

    byte[] compressData();

    Map<Integer, List<PacketWrapper>> getPackets(UUID uuid);

    Collection<UUID> getPlayers();
}
