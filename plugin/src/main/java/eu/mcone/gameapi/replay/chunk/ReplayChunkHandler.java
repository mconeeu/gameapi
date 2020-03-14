package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.core.util.GenericUtils;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.replay.session.ReplaySession;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReplayChunkHandler implements eu.mcone.gameapi.api.replay.chunk.ReplayChunkHandler {

    private ReplaySession session;
    private LinkedHashMap<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks;
    @Getter
    private File replayFile;
    @Getter
    private int currentChunkID;
    private BukkitTask unloadChunkTask;

    public ReplayChunkHandler(ReplaySession session) {
        this.session = session;
        chunks = new LinkedHashMap<>();
    }

    private void unloadChunk() {
        if (unloadChunkTask == null) {
            unloadChunkTask = Bukkit.getScheduler().runTaskTimerAsynchronously(GameAPIPlugin.getInstance(), () -> {
                if (chunks.size() > 0 && chunks.size() > 3) {
                    List<Integer> whitelist = new ArrayList<>();
                    whitelist.add(currentChunkID - 1);
                    whitelist.add(currentChunkID);
                    whitelist.add(currentChunkID + 1);

                    for (int i = 0; i < chunks.size(); i++) {
                        if (chunks.containsKey(i) && !whitelist.contains(i)) {
                            System.out.println("Remove chunk " + i);
                            chunks.remove(i);
                        }
                    }
                }
            }, 120, 120);
        }
    }

    public void preLoad() {
        setPath();

        if (currentChunkID < 2) {
            for (int i = 0; i <= 2; i++) {
                loadChunk(i);
            }
        }
    }

    public eu.mcone.gameapi.api.replay.chunk.ReplayChunk getChunk(int tick) {
        currentChunkID = tick / 600;

        System.out.println("Return chunk");
        if (!chunks.containsKey(currentChunkID + 1)) {
            loadChunk(currentChunkID);
        }

        return chunks.get(currentChunkID);
    }

    private void loadChunk(int chunkID) {
        if (!chunks.containsKey(chunkID)) {
            setPath();

            Bukkit.getScheduler().runTaskAsynchronously(GameAPIPlugin.getInstance(), () -> {
                try {
                    if (replayFile.exists()) {
                        System.out.println("Loading chunk...");
                        FileInputStream fileInputStream = new FileInputStream(replayFile);
                        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);

                        for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null; ) {
                            if (zipEntry.getName().equalsIgnoreCase("CHUNK:" + chunkID)) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                try {
                                    final byte[] buf = new byte[1024];
                                    int length;
                                    while ((length = zipInputStream.read(buf, 0, buf.length)) >= 0) {
                                        byteArrayOutputStream.write(buf, 0, length);
                                    }

                                    byte[] compressedData = byteArrayOutputStream.toByteArray();

                                    //Uncompress
                                    Inflater inflater = new Inflater();
                                    inflater.setInput(compressedData);
                                    byteArrayOutputStream = new ByteArrayOutputStream(compressedData.length);
                                    while (!inflater.finished()) {
                                        int count = inflater.inflate(buf);
                                        byteArrayOutputStream.write(buf, 0, count);
                                    }

                                    ReplayChunk chunk = (ReplayChunk) GenericUtils.deserialize(byteArrayOutputStream.toByteArray());
                                    chunks.put(chunkID, chunk);
                                    System.out.println("Chunk loaded!");

                                    unloadChunk();
                                    byteArrayOutputStream.close();
                                } catch (IOException | DataFormatException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        zipInputStream.close();
                        fileInputStream.close();
                        System.gc();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void setPath() {
        replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", session.getID() + ".replay");
    }
}
