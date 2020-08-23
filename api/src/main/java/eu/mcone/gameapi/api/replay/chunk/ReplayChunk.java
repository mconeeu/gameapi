package eu.mcone.gameapi.api.replay.chunk;

import eu.mcone.coresystem.api.bukkit.codec.Codec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReplayChunk {

    int getID();

    ChunkData getChunkData();

    void addServerPacket(int tick, Codec<?, ?> wrapper);

    void addPacket(UUID uuid, int tick, Codec<?, ?> wrapper);

    Map<Integer, List<Codec<?, ?>>> getPackets(UUID uuid);

    List<Codec<?, ?>> getServerCodecs(int tick);

    Codec<?, ?> getLastServerPacketInRange(int start, int end);

    Codec<?, ?> getLastPlayerPacketInRange(UUID uuid, int startTick, int endTick);

    Collection<UUID> getPlayers();

    interface ChunkData {

        Map<UUID, Map<Integer, List<Codec<?, ?>>>> getPlayerCodecs();

        Map<Integer, List<Codec<?, ?>>> getServerCodecs();

        byte[] serialize();

        byte[] deserialize();

        int getLength();
    }
}
