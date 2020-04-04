package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.core.util.GenericUtils;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.replay.event.ReplayStopEvent;
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
    private int lastChunkID;
    private BukkitTask unloadChunkTask;

    public ReplayChunkHandler(ReplaySession session) {
        this.session = session;
        chunks = new LinkedHashMap<>();
        lastChunkID = session.getInfo().getLastTick() / 600;
    }

    private void unloadChunk() {
        if (unloadChunkTask == null) {
            unloadChunkTask = Bukkit.getScheduler().runTaskTimer(GameAPIPlugin.getInstance(), () -> {
                if (currentChunkID > 2) {
                    List<Integer> whitelist = new ArrayList<>();
                    whitelist.add(currentChunkID - 1);
                    whitelist.add(currentChunkID);
                    whitelist.add(currentChunkID + 1);

                    for (int i = 0; i < chunks.size(); i++) {
                        if (chunks.containsKey(i) && !whitelist.contains(i)) {
                            chunks.remove(i);
                            System.out.println("REMOVE Chunk: " + i);
                        }
                    }
                }
            }, 120, 120);
        }
    }

    public void preLoad() {
        set();

        if (currentChunkID < 2) {
            for (int i = 0; i <= 2; i++) {
                loadChunk(i);
            }
        }
    }

    public eu.mcone.gameapi.api.replay.chunk.ReplayChunk getChunk(int tick) {
        currentChunkID = tick / 600;

        System.out.println("CURRENT ID: " + currentChunkID);
        int nextChunk = currentChunkID + 1;
        if (tick != session.getInfo().getLastTick()) {
            if (!chunks.containsKey(nextChunk) && nextChunk <= lastChunkID) {
                System.out.println("Load next CHUNK " + nextChunk);
                loadChunk(nextChunk);
            }

            System.out.println("DEBUG-2");
        } else {
            Bukkit.getScheduler().cancelTask(unloadChunkTask.getTaskId());
            System.out.println("DEBUG-3");
        }

        return chunks.get(currentChunkID);
    }

    private void loadChunk(int chunkID) {
        if (!chunks.containsKey(chunkID)) {
            set();

            Bukkit.getScheduler().runTask(GameAPIPlugin.getInstance(), () -> {
                if (replayFile.exists()) {
                    FileInputStream fileInputStream = null;
                    ZipInputStream zipInputStream = null;

                    System.out.println("LOAD CHUNK: " + chunkID);

                    try {
                        fileInputStream = new FileInputStream(replayFile);
                        zipInputStream = new ZipInputStream(fileInputStream);

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

                                    ReplayChunk chunk = new ReplayChunk((ReplayChunk.ChunkData) GenericUtils.deserialize(byteArrayOutputStream.toByteArray()));
                                    System.out.println(chunk.getPlayers().size());
                                    chunks.put(chunkID, chunk);

                                    unloadChunk();
                                } catch (IOException | DataFormatException ex) {
                                    ex.printStackTrace();
                                } finally {
                                    byteArrayOutputStream.close();
                                }
                            }
                        }

                        zipInputStream.close();
                        fileInputStream.close();
                        System.gc();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (zipInputStream != null) {
                                zipInputStream.close();
                            }

                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Bukkit.getPluginManager().callEvent(new ReplayStopEvent(session, 0));
                    throw new NullPointerException("Replay file not found");
                }
            });
        }
    }

    private void set() {
        if (replayFile == null) {
            replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", session.getID() + ".replay");
            lastChunkID = session.getInfo().getLastTick() / 1200;
        }
    }
}
