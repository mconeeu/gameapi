package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.packets.Chunk;
import eu.mcone.coresystem.api.core.util.CompressUtils;
import eu.mcone.coresystem.api.core.util.GenericUtils;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class ReplayChunk extends Chunk implements Serializable, eu.mcone.gameapi.api.replay.chunk.ReplayChunk {

    private final ChunkData chunkData;

    public ReplayChunk() {
        chunkData = new ChunkData();
    }

    public ReplayChunk(ChunkData chunkData) {
        this.chunkData = chunkData;
    }

    public void addServerPacket(int tick, Codec<?, ?> codec) {
        if (codec != null) {
            if (chunkData.serverCodecs.containsKey(tick)) {
                chunkData.serverCodecs.get(tick).add(codec);
            } else {
                chunkData.serverCodecs.put(tick, new ArrayList<Codec<?, ?>>() {{
                    add(codec);
                }});
            }
        }
    }

    public void addPacket(UUID uuid, int tick, Codec<?, ?> codec) {
        if (codec != null) {
            if (chunkData.playerCodecs.containsKey(uuid)) {
                if (chunkData.playerCodecs.get(uuid).containsKey(tick)) {
                    chunkData.playerCodecs.get(uuid).get(tick).add(codec);
                } else {
                    chunkData.playerCodecs.get(uuid).put(tick, new ArrayList<Codec<?, ?>>() {{
                        add(codec);
                    }});
                }
            } else {
                chunkData.playerCodecs.put(uuid, new HashMap<Integer, List<Codec<?, ?>>>() {{
                    put(tick, new ArrayList<Codec<?, ?>>() {{
                        add(codec);
                    }});
                }});
            }
        }
    }

    public Codec<?, ?> getLastPacketInRange(UUID uuid, Object obj, int startTick, int endTick) {
        if (this.chunkData.playerCodecs.containsKey(uuid)) {
            Codec<?, ?> found = null;
            int tick = startTick;
            do {
                List<Codec<?, ?>> packets = this.chunkData.playerCodecs.get(uuid).get(tick);
                if (packets != null) {
                    for (Codec<?, ?> packet : packets) {
                        if (packet.getClass().isInstance(obj)) {
                            found = packet;
                        }
                    }
                }

                if (endTick < startTick) {
                    tick--;
                } else {
                    tick++;
                }

            } while (tick != endTick);

            return found;
        } else {
            return null;
        }
    }

    public byte[] compressData() {
        return CompressUtils.compress(GenericUtils.serialize(chunkData));
    }

    public Map<Integer, List<Codec<?, ?>>> getPackets(UUID uuid) {
        return chunkData.playerCodecs.get(uuid);
    }

    public List<Codec<?, ?>> getServerCodecs(final int tick) {
        return chunkData.serverCodecs.getOrDefault(tick, null);
    }

    public Collection<UUID> getPlayers() {
        return chunkData.playerCodecs.keySet();
    }

    @Getter
    public static class ChunkData extends eu.mcone.coresystem.api.bukkit.packets.ChunkData implements eu.mcone.gameapi.api.replay.chunk.ReplayChunk.ChunkData {
        private final Map<UUID, Map<Integer, List<Codec<?, ?>>>> playerCodecs;
        private final Map<Integer, List<Codec<?, ?>>> serverCodecs;

        public ChunkData() {
            playerCodecs = new HashMap<>();
            serverCodecs = new HashMap<>();
        }

        @Override
        public int getLength() {
            return playerCodecs.size() + serverCodecs.size();
        }
    }
}