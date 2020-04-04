package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
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

    public void addServerPacket(int tick, PacketWrapper wrapper) {
        if (wrapper != null) {
            if (chunkData.serverPackets.containsKey(tick)) {
                chunkData.serverPackets.get(tick).add(wrapper);
            } else {
                chunkData.serverPackets.put(tick, new ArrayList<PacketWrapper>() {{
                    add(wrapper);
                }});
            }
        }
    }

    public void addPacket(UUID uuid, int tick, PacketWrapper wrapper) {
        if (wrapper != null) {
            if (chunkData.playerPackets.containsKey(uuid)) {
                if (chunkData.playerPackets.get(uuid).containsKey(tick)) {
                    chunkData.playerPackets.get(uuid).get(tick).add(wrapper);
                } else {
                    chunkData.playerPackets.get(uuid).put(tick, new ArrayList<PacketWrapper>() {{
                        add(wrapper);
                    }});
                }
            } else {
                chunkData.playerPackets.put(uuid, new HashMap<Integer, List<PacketWrapper>>() {{
                    put(tick, new ArrayList<PacketWrapper>() {{
                        add(wrapper);
                    }});
                }});
            }
        }
    }

    public PacketWrapper getLastPacketInRange(UUID uuid, Object obj, int startTick, int endTick) {
        if (this.chunkData.playerPackets.containsKey(uuid)) {
            PacketWrapper found = null;
            int tick = startTick;
            while (true) {
                List<PacketWrapper> packets = this.chunkData.playerPackets.get(uuid).get(tick);
                if (packets != null) {
                    for (PacketWrapper packet : packets) {
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

    public Map<Integer, List<PacketWrapper>> getPackets(UUID uuid) {
        System.out.println("CONTAINS: " + chunkData.playerPackets.containsKey(uuid));
        return chunkData.playerPackets.get(uuid);
    }

    public List<PacketWrapper> getServerPackets(final Integer tick) {
        return chunkData.serverPackets.getOrDefault(tick, null);
    }

    public Collection<UUID> getPlayers() {
        return chunkData.playerPackets.keySet();
    }

    @Getter
    public class ChunkData implements Serializable, eu.mcone.gameapi.api.replay.chunk.ReplayChunk.ChunkData {
        private Map<UUID, Map<Integer, List<PacketWrapper>>> playerPackets;
        private Map<Integer, List<PacketWrapper>> serverPackets;

        public ChunkData() {
            playerPackets = new HashMap<>();
            serverPackets = new HashMap<>();
        }

        public int getPacketsAmount() {
            return playerPackets.size() + serverPackets.size();
        }
    }
}
