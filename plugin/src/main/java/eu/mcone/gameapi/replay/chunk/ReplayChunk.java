package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.codec.CodecInputStream;
import eu.mcone.coresystem.api.bukkit.codec.CodecOutputStream;
import eu.mcone.coresystem.api.bukkit.codec.DeserializeCallback;
import eu.mcone.coresystem.api.bukkit.packets.Chunk;
import eu.mcone.coresystem.api.core.util.GenericUtils;
import eu.mcone.gameapi.api.GamePlugin;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class ReplayChunk extends Chunk implements Serializable, eu.mcone.gameapi.api.replay.chunk.ReplayChunk {

    @Getter
    private final ChunkData chunkData;
    @Getter
    private final int ID;

    public ReplayChunk(int ID) {
        chunkData = new ChunkData();
        this.ID = ID;
    }

    public ReplayChunk(int ID, ChunkData chunkData) {
        this.ID = ID;
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
        private final transient Map<UUID, Map<Integer, List<Codec<?, ?>>>> playerCodecs;
        private final transient Map<Integer, List<Codec<?, ?>>> serverCodecs;

        private Map<UUID, Map<Integer, byte[]>> genericPlayerCodecs;
        private Map<Integer, byte[]> genericServerCodecs;

        public ChunkData() {
            playerCodecs = new HashMap<>();
            serverCodecs = new HashMap<>();

            genericPlayerCodecs = new HashMap<>();
            genericServerCodecs = new HashMap<>();
        }

        public byte[] serialize() {
            CodecOutputStream codecOutputStream = new CodecOutputStream();

            for (Map.Entry<UUID, Map<Integer, List<Codec<?, ?>>>> entry : playerCodecs.entrySet()) {
                for (Map.Entry<Integer, List<Codec<?, ?>>> innerEntry : entry.getValue().entrySet()) {
                    if (!genericPlayerCodecs.containsKey(entry.getKey())) {
                        genericPlayerCodecs.put(entry.getKey(), new HashMap<>());
                    }

                    genericPlayerCodecs.get(entry.getKey()).put(innerEntry.getKey(), codecOutputStream.serialize(innerEntry.getValue()));
                }
            }

            for (Map.Entry<Integer, List<Codec<?, ?>>> entry : serverCodecs.entrySet()) {
                genericServerCodecs.put(entry.getKey(), codecOutputStream.serialize(entry.getValue()));
            }

            return GenericUtils.serialize(this);
        }

        public byte[] deserialize() {
            CodecInputStream codecInputStream = new CodecInputStream(GamePlugin.getGamePlugin().getReplayManager().getCodecRegistry());
            boolean migrate = false;

            Map<UUID, Map<Integer, byte[]>> tmpGenericPlayerCodecs = genericPlayerCodecs;
            Map<Integer, byte[]> tmpGenericServerCodecs = genericServerCodecs;

            for (Map.Entry<UUID, Map<Integer, byte[]>> entry : genericPlayerCodecs.entrySet()) {
                for (Map.Entry<Integer, byte[]> innerEntry : entry.getValue().entrySet()) {
                    DeserializeCallback callback = codecInputStream.deserialize(innerEntry.getValue());

                    if (playerCodecs.containsKey(entry.getKey())) {
                        playerCodecs.get(entry.getKey()).put(innerEntry.getKey(), callback.getCodecs());

                        if (callback.getMigrated() != 0) {
                            tmpGenericPlayerCodecs.get(entry.getKey()).put(innerEntry.getKey(), callback.getMigratedCodecs());
                        }
                    } else {
                        playerCodecs.put(entry.getKey(), new HashMap<Integer, List<Codec<?, ?>>>() {{
                            put(innerEntry.getKey(), callback.getCodecs());
                        }});


                        if (callback.getMigrated() != 0) {
                            genericPlayerCodecs.put(entry.getKey(), new HashMap<Integer, byte[]>() {{
                                put(innerEntry.getKey(), callback.getMigratedCodecs());
                            }});
                        }
                    }

                    if (!migrate) {
                        migrate = callback.getMigrated() != 0;
                    }
                }
            }

            for (Map.Entry<Integer, byte[]> entry : genericServerCodecs.entrySet()) {
                DeserializeCallback callback = codecInputStream.deserialize(entry.getValue());
                serverCodecs.put(entry.getKey(), callback.getCodecs());

                if (callback.getMigrated() != 0) {
                    genericServerCodecs.put(entry.getKey(), callback.getMigratedCodecs());
                }

                if (!migrate) {
                    migrate = callback.getMigrated() != 0;
                }
            }

            if (migrate) {
                genericPlayerCodecs = tmpGenericPlayerCodecs;
                genericServerCodecs = tmpGenericServerCodecs;
                return GenericUtils.serialize(this);
            }

            System.gc();
            return null;
        }

        @Override
        public int getLength() {
            return playerCodecs.size() + serverCodecs.size();
        }
    }
}