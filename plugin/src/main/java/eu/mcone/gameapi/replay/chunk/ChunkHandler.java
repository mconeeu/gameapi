package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.packets.ChunkData;
import eu.mcone.coresystem.api.core.util.CompressUtils;
import eu.mcone.coresystem.api.core.util.GenericUtils;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.event.chunk.ReplayChunkLoadedEvent;
import eu.mcone.gameapi.api.replay.event.chunk.ReplayChunkUnloadedEvent;
import eu.mcone.gameapi.api.replay.event.runner.ReplayStopEvent;
import eu.mcone.gameapi.api.replay.exception.ReplaySessionAlreadyExistsException;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.replay.session.Replay;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ChunkHandler implements eu.mcone.gameapi.api.replay.chunk.ReplayChunkHandler, Serializable {

    private transient final Replay replay;
    private final Map<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks;
    @Getter
    private transient File replayFile;
    @Getter
    private transient int currentChunkID;
    private transient int lastChunkID;
    private transient BukkitTask unloadChunkTask;

    public ChunkHandler(final Replay replay) {
        this.replay = replay;
        chunks = new HashMap<>();
        lastChunkID = replay.getLastTick() / 600;
        replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", replay.getID() + ".replay");
    }

    public ChunkHandler(final Replay replay, Map<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks) {
        this.replay = replay;
        this.chunks = chunks;
        lastChunkID = replay.getLastTick() / 600;
        replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", replay.getID() + ".replay");
    }

    public ReplayChunk createNewChunk() {
        return new ReplayChunk();
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
                            Bukkit.getPluginManager().callEvent(new ReplayChunkUnloadedEvent(replay.getID(), "CHUNK:" + i));
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
        if (tick != replay.getLastTick()) {
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

    public File save() {
        System.out.println("SAVE REPLAY");
        String replayID = replay.getID();

        boolean succeed = true;
        if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(replay.getWorld())) {
            succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(replay.getWorld()));
        }

        if (succeed) {
            System.out.println("World upload Succeeded, create and save now the replay file...");
            Bukkit.getScheduler().runTaskAsynchronously(GameAPIPlugin.getInstance(), () -> {
                List<File> zipFiles = new ArrayList<>();

                for (Map.Entry<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> entry : chunks.entrySet()) {
                    File file = new File("CHUNK:" + entry.getKey());
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(entry.getValue().compressData());
                        zipFiles.add(file);
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/" + replayID + ".replay");
                    FileOutputStream fos = new FileOutputStream(replayFile);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);
                    for (File file : zipFiles) {
                        FileInputStream fis = new FileInputStream(file);
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }

                        fis.close();
                        file.delete();
                    }

                    zipOut.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        return replayFile;
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
                                    ReplayChunk chunk = new ReplayChunk(GenericUtils.deserialize(ReplayChunk.ChunkData.class, CompressUtils.unCompress(compressedData)));
                                    System.out.println(chunk.getPlayers().size());
                                    chunks.put(chunkID, chunk);
                                    Bukkit.getPluginManager().callEvent(new ReplayChunkLoadedEvent(replay.getID(), "CHUNK:" + chunkID));

                                    unloadChunk();
                                } catch (IOException ex) {
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
                    throw new NullPointerException("Replay file not found");
                }
            });
        }
    }

    private void set() {
        if (replayFile == null) {
            replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", replay.getID() + ".replay");
            lastChunkID = replay.getLastTick() / 1200;
        }
    }
}
