package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.coresystem.api.core.util.GenericUtils;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.zip.Deflater;

public class ReplayChunk implements Serializable, eu.mcone.gameapi.api.replay.chunk.ReplayChunk {

    private ChunkData chunkData;

    public ReplayChunk() {
        chunkData = new ChunkData();
    }

    public ReplayChunk(ChunkData data) {
        this.chunkData = data;
    }

    public void addServerPacket(int tick, PacketContainer wrapper) {
        if (wrapper != null) {
            if (chunkData.serverPackets.containsKey(tick)) {
                chunkData.serverPackets.get(tick).add(wrapper);
            } else {
                chunkData.serverPackets.put(tick, new ArrayList<PacketContainer>() {{
                    add(wrapper);
                }});
            }
        }
    }

    public void addPacket(UUID uuid, int tick, PacketContainer wrapper) {
        if (wrapper != null) {
            if (chunkData.playerPackets.containsKey(uuid)) {
                if (chunkData.playerPackets.get(uuid).containsKey(tick)) {
                    chunkData.playerPackets.get(uuid).get(tick).add(wrapper);
                } else {
                    chunkData.playerPackets.get(uuid).put(tick, new ArrayList<PacketContainer>() {{
                        add(wrapper);
                    }});
                }
            } else {
                chunkData.playerPackets.put(uuid, new HashMap<Integer, List<PacketContainer>>() {{
                    put(tick, new ArrayList<PacketContainer>() {{
                        add(wrapper);
                    }});
                }});
            }
        }
    }

    public PacketContainer getLastPacketInRange(UUID uuid, Object obj, int startTick, int endTick) {
        if (this.chunkData.playerPackets.containsKey(uuid)) {
            PacketContainer found = null;
            int tick = startTick;
            while (true) {
                List<PacketContainer> packets = this.chunkData.playerPackets.get(uuid).get(tick);
                if (packets != null) {
                    for (PacketContainer packet : packets) {
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

                if (tick == endTick) {
                    break;
                }
            }

            return found;
        } else {
            return null;
        }
    }

    public byte[] compressData() {
        try {
            byte[] unCompressedData = GenericUtils.serialize(chunkData);
            Deflater deflater = new Deflater();
            deflater.setInput(unCompressedData);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(unCompressedData.length);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<Integer, List<PacketContainer>> getPackets(UUID uuid) {
        System.out.println("CONTAINS: " + chunkData.playerPackets.containsKey(uuid));
        return chunkData.playerPackets.get(uuid);
    }

    public List<PacketContainer> getServerPackets(final Integer tick) {
        return chunkData.serverPackets.getOrDefault(tick, null);
    }

    public Collection<UUID> getPlayers() {
        return chunkData.playerPackets.keySet();
    }

    @Getter
    public static class ChunkData implements Serializable, eu.mcone.gameapi.api.replay.chunk.ReplayChunk.ChunkData {
        private final Map<UUID, Map<Integer, List<PacketContainer>>> playerPackets;
        private final Map<Integer, List<PacketContainer>> serverPackets;

        public ChunkData() {
            playerPackets = new HashMap<>();
            serverPackets = new HashMap<>();
        }

        public int getPacketsAmount() {
            return playerPackets.size() + serverPackets.size();
        }
    }
}
