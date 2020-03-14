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

    private Map<UUID, Map<Integer, List<PacketWrapper>>> player;
    @Getter
    private int chunkID;

    public ReplayChunk(int chunkID) {
        player = new HashMap<>();
        this.chunkID = chunkID;
    }

    public void addPacket(UUID uuid, int tick, PacketWrapper wrapper) {
        if (player.containsKey(uuid)) {
            if (player.get(uuid).containsKey(tick)) {
                player.get(uuid).get(tick).add(wrapper);
            } else {
                player.get(uuid).put(tick, new ArrayList<PacketWrapper>() {{
                    add(wrapper);
                }});
            }
        } else {
            player.put(uuid, new HashMap<Integer, List<PacketWrapper>>() {{
                put(tick, new ArrayList<PacketWrapper>() {{
                    add(wrapper);
                }});
            }});
        }
    }

    public byte[] compressData() {
        try {
            byte[] unCompressedData = GenericUtils.serialize(this);
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
        return player.get(uuid);
    }

    public Collection<UUID> getPlayers() {
        return player.keySet();
    }
}
