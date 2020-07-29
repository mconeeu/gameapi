package eu.mcone.gameapi.replay.chunk;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.core.util.GenericUtils;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.event.chunk.ReplayChunkLoadedEvent;
import eu.mcone.gameapi.api.replay.event.chunk.ReplayChunkUnloadedEvent;
import eu.mcone.gameapi.replay.session.Replay;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ChunkHandler implements eu.mcone.gameapi.api.replay.chunk.ReplayChunkHandler, Serializable {

    private transient final Replay replay;
    private final Map<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks;
    @Getter
    private transient File replayFile;
    private transient final File tmpDir;
    private transient int currentChunkID;
    private transient int lastChunkID;
    private transient BukkitTask unloadChunkTask;

    private final HashMap<Integer, byte[]> toMigrate;

    public ChunkHandler(final Replay replay) {
        this.replay = replay;
        chunks = new HashMap<>();
        lastChunkID = replay.getLastTick() / 600;
        replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", replay.getID() + ".replay");
        tmpDir = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/");

        toMigrate = new HashMap<>();
    }

    public ChunkHandler(final Replay replay, Map<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks) {
        this.replay = replay;
        this.chunks = chunks;
        lastChunkID = replay.getLastTick() / 600;
        replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/", replay.getID() + ".replay");
        tmpDir = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/");

        toMigrate = new HashMap<>();
    }

    public ReplayChunk createNewChunk(int ID) {
        return new ReplayChunk(ID);
    }

    private void unloadChunk() {
        if (unloadChunkTask == null) {
            unloadChunkTask = Bukkit.getScheduler().runTaskTimerAsynchronously(GameAPIPlugin.getInstance(), () -> {
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

        } else {
            Bukkit.getScheduler().cancelTask(unloadChunkTask.getTaskId());
        }

        return chunks.get(currentChunkID);
    }

    public File save() {
        boolean succeed = true;
        if (!CoreSystem.getInstance().getWorldManager().existsWorldInDatabase(replay.getWorld())) {
            succeed = CoreSystem.getInstance().getWorldManager().upload(CoreSystem.getInstance().getWorldManager().getWorld(replay.getWorld()));
        }

        if (succeed) {
            System.out.println("World upload Succeeded, create and save now the replay file...");
            Bukkit.getScheduler().runTaskAsynchronously(GameAPIPlugin.getInstance(), () -> {
                try {
                    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(replayFile));
                    for (Map.Entry<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> entry : chunks.entrySet()) {
                        try {
                            ZipEntry zipEntry = new ZipEntry("CHUNK:" + entry.getKey());
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(entry.getValue().getChunkData().serialize());

                            zipOut.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    zipOut.close();
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

                    try {
                        fileInputStream = new FileInputStream(replayFile);
                        zipInputStream = new ZipInputStream(fileInputStream);

                        for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null; ) {
                            if (zipEntry.getName().equalsIgnoreCase("CHUNK:" + chunkID)) {
                                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                                    final byte[] buf = new byte[1024];
                                    int length;
                                    while ((length = zipInputStream.read(buf, 0, buf.length)) >= 0) {
                                        byteArrayOutputStream.write(buf, 0, length);
                                    }

                                    ReplayChunk chunk = new ReplayChunk(chunkID, GenericUtils.deserialize(ReplayChunk.ChunkData.class, byteArrayOutputStream.toByteArray()));
                                    byte[] migrated = chunk.getChunkData().deserialize();

                                    if (migrated != null) {
                                        toMigrate.put(chunkID, migrated);
                                    }

                                    System.out.println(chunk.getPlayers().size());
                                    chunks.put(chunkID, chunk);
                                    Bukkit.getPluginManager().callEvent(new ReplayChunkLoadedEvent(replay.getID(), "CHUNK:" + chunkID));

                                    unloadChunk();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        zipInputStream.close();
                        fileInputStream.close();
                        System.gc();

                        migrate();
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

    private void migrate() {
        if (toMigrate.size() > 0) {
            Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getGamePlugin(), () -> {
                GameAPIPlugin.getInstance().sendConsoleMessage("§aStarting migration...");

                List<byte[]> data = new ArrayList<>();

                //Read file
                FileInputStream fileInputStream = null;
                ZipInputStream zipInputStream = null;

                try {
                    fileInputStream = new FileInputStream(replayFile);
                    zipInputStream = new ZipInputStream(fileInputStream);

                    for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null; ) {
                        for (Map.Entry<Integer, byte[]> migrationEntry : toMigrate.entrySet()) {
                            if (zipEntry.getName().equalsIgnoreCase("CHUNK:" + migrationEntry.getKey())) {
                                data.add(migrationEntry.getValue());
                            } else {
                                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                                    final byte[] buf = new byte[1024];
                                    int length;
                                    while ((length = zipInputStream.read(buf, 0, buf.length)) >= 0) {
                                        byteArrayOutputStream.write(buf, 0, length);
                                    }

                                    data.add(byteArrayOutputStream.toByteArray());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }

                    zipInputStream.close();
                    fileInputStream.close();
                    System.gc();

                    migrate();
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

                if (!data.isEmpty()) {
                    //Write new file
                    GamePlugin.getGamePlugin().sendConsoleMessage("§aWriting migrated chunks to file...");

                    replayFile = new File("./plugins/" + GameAPIPlugin.getInstance().getPluginName() + "/" + replay.getID() + ".replay");

                    if (replayFile.exists()) {
                        if (replayFile.delete()) {
                            GamePlugin.getGamePlugin().sendConsoleMessage("§aDeleted old replay file!");
                        }
                    }

                    try {
                        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(replayFile));

                        int chunkID = 0;
                        for (byte[] chunkData : data) {
                            ZipEntry zipEntry = new ZipEntry("CHUNK:" + chunkID);
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(chunkData);

                            zipOut.closeEntry();

                            chunkID++;
                        }

                        zipOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    GamePlugin.getGamePlugin().sendConsoleMessage("§aMigration completed!");
                } else {
                    GamePlugin.getGamePlugin().sendConsoleMessage("§cError by migrating Chunk data to newer Version, ID " + replay.getID() + " (empty chunk data!)");
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
