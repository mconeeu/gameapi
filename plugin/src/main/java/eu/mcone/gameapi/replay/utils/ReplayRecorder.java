package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.codec.CodecListener;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.npc.capture.Recorder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.ReplayRecord;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;
import eu.mcone.gameapi.api.utils.IDUtils;
import eu.mcone.gameapi.replay.chunk.ReplayChunk;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

@Getter
public class ReplayRecorder extends Recorder implements eu.mcone.gameapi.api.replay.utils.ReplayRecorder {

    private final HashMap<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks;
    private int lastTick;

    private BukkitTask task;

    private final ReplayRecord replayRecord;
    private final CodecRegistry registry;
    private CodecListener codecListener;

    public ReplayRecorder(ReplayRecord replayRecord, CodecRegistry registry) {
        super(IDUtils.generateID(), GamePlugin.getGamePlugin().getGameConfig().parseConfig().getGameWorld());
        this.replayRecord = replayRecord;
        chunks = new HashMap<>();
        this.registry = registry;
    }

    @Override
    public void record() {
        registry.listeningForCodecs(true);
        started = System.currentTimeMillis() / 1000;
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(CoreSystem.getInstance(), () -> ticks++, 1L, 1L);

        Bukkit.getScheduler().runTaskAsynchronously(GamePlugin.getGamePlugin(), () -> {
            this.codecListener = (codec, objects) -> {
                if (objects != null) {
                    System.out.println(codec.getClass().getSimpleName());
                    Class<?> trigger = registry.getTriggerClass(codec.getCodecID());
                    if (trigger != null) {
                        if (registry.getTriggerClass(codec.getCodecID()).equals(PlayerMoveEvent.class)) {
                            if ((ticks % 2) == 0) {
                                addCodec((Player) objects[0], codec);
                            }
                        } else {
                            addCodec((Player) objects[0], codec);
                        }
                    }
                } else {
                    addServerCodec(codec);
                }
            };

            registry.registerCodecListener(codecListener);
        });
    }

    private void addServerCodec(Codec<?, ?> codec) {
        int chunkID = ticks / 600;
        lastTick = ticks;

        if (chunks.containsKey(chunkID)) {
            chunks.get(chunkID).addServerPacket(ticks, codec);
        } else {
            ReplayChunk chunk = new ReplayChunk(chunkID);
            chunk.addServerPacket(ticks, codec);
            chunks.put(chunkID, chunk);
        }
    }

    private void addCodec(Player player, Codec<?, ?> codec) {
        if (replayRecord.existsReplayPlayer(player)) {
            int chunkID = ticks / 600;
            lastTick = ticks;

            if (chunks.containsKey(chunkID)) {
                System.out.println("ADD CODEC " + codec.getClass().getSimpleName());
                chunks.get(chunkID).addPacket(player.getUniqueId(), ticks, codec);
            } else {
                System.out.println("NEW REPLAY CHUNK");
                ReplayChunk chunk = new ReplayChunk(chunkID);
                chunk.addPacket(player.getUniqueId(), ticks, codec);
                chunks.put(chunkID, chunk);
            }
        }
    }

    public void stop() {
        stopped = System.currentTimeMillis() / 1000;
        stop = true;
        if (task != null) {
            task.cancel();
        }
        registry.listeningForCodecs(false);
        registry.unregisterCodecListener(codecListener);
    }

    public void addMessage(MessageWrapper wrapper) {
        this.getReplayRecord().getGameHistory().addMessage(ticks, wrapper);
    }
}
