package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.codec.CodecRegistry;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.npc.enums.NpcAnimation;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.replay.container.ReplayContainer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerRunner extends eu.mcone.coresystem.api.bukkit.npc.capture.Player implements eu.mcone.gameapi.api.replay.runner.PlayerRunner {

    @Getter
    private final ReplayPlayer player;
    @Getter
    private final ReplayContainer container;

    @Getter
    private final Map<Integer, List<org.bukkit.block.Block>> placedBlocks;
    @Getter
    private final Map<Integer, List<org.bukkit.block.Block>> brakedBlocks;

    @Setter
    @Getter
    private ReplaySpeed replaySpeed = ReplaySpeed._1X;

    private final ScheduledThreadPoolExecutor executor;

    @Setter
    private boolean breaking = false;

    private final CodecRegistry codecRegistry;

    public PlayerRunner(final ReplayPlayer player, final ReplayContainer container) {
        this.codecRegistry = GamePlugin.getGamePlugin().getReplayManager().getCodecRegistry();
        this.player = player;
        this.container = container;
        placedBlocks = new HashMap<>();
        brakedBlocks = new HashMap<>();
        currentTick = new AtomicInteger();
        this.executor = (ScheduledThreadPoolExecutor) Executors.newSingleThreadExecutor();
    }

    @Override
    public void play() {
        if (!playing) {
            currentTick.set(0);
        }

        executor.schedule(() -> {
            if (playing) {
                int tick = currentTick.get();
                ReplayChunk chunk = container.getChunkHandler().getChunk(tick);
                Map<Integer, List<Codec<?, ?>>> codecs = chunk.getPackets(player.getUuid());

//                int repeat;
//                if (replaySpeed != null && skipped == replaySpeed.getWait()) {
//                    repeat = (replaySpeed.isAdd() ? 2 : 0);
//                    skipped = 0;
//                } else {
//                    repeat = 1;
//                }

                if (tick != container.getReplay().getLastTick()) {
                    if (breaking) {
                        player.getNpc().sendAnimation(NpcAnimation.SWING_ARM, watcher.toArray(new Player[0]));
                    }

                    if (codecs.containsKey(tick)) {
                        for (Codec codec : codecs.get(tick)) {
                            if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(PlayerNpc.class)) {
                                codec.encode(player.getNpc());
                            } else if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(eu.mcone.gameapi.api.replay.runner.PlayerRunner.class)) {
                                codec.encode(this);
                            } else if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(eu.mcone.gameapi.replay.player.ReplayPlayer.class)) {
                                codec.encode(player);
                            }
                        }

//                        skipped++;
                    }


                    if (forward) {
                        this.currentTick.getAndIncrement();
                    } else if (backward) {
                        if (tick >= 1) {
                            currentTick.getAndDecrement();
                        } else {
                            forward = true;
                            currentTick.getAndIncrement();
                        }
                    }
                } else {
                    playing = false;
                    Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.END));
                }
            }
        }, replaySpeed.getSpeed(), TimeUnit.MILLISECONDS);

        if (playing) {
            play();
        }
    }

    @Override
    public void stop() {
        super.stop();
        CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
        container.getNpcListener().setUnregister(true);
    }

    public void skip(int skipTicks) {
        if (playing) {
            //Set blocks
            int cTick = currentTick.get();
            int end;

            ReplayChunk chunk;
            Map<Integer, List<Codec<?, ?>>> codecs = null;

            if (skipTicks > 0) {
                chunk = container.getChunkHandler().getChunk(currentTick.get());
                codecs = chunk.getPackets(player.getUuid());
            }

            end = cTick + skipTicks;

            //Check if the end tick is higher then the last tick
            if (end > container.getReplay().getLastTick()) {
                end = container.getReplay().getLastTick();
            } else if (end < 0) {
                end = 0;
            }

            do {
                if (codecs == null) {
                    if (placedBlocks.containsKey(cTick)) {
                        if (!placedBlocks.get(cTick).isEmpty()) {
                            for (org.bukkit.block.Block block : placedBlocks.get(cTick)) {
                                sendBlockUpdate(block.getLocation(), Material.AIR, (byte) 0);
                            }
                        }
                    } else if (brakedBlocks.containsKey(cTick)) {
                        if (!brakedBlocks.get(cTick).isEmpty()) {
                            for (org.bukkit.block.Block block : brakedBlocks.get(cTick)) {
                                sendBlockUpdate(block.getLocation(), block.getType(), block.getData());
                            }
                        }
                    }

                    cTick--;
                } else {
                    if (codecs.containsKey(cTick)) {
                        for (Codec codec : codecs.get(cTick)) {
                            if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(PlayerNpc.class)) {
                                codec.encode(player.getNpc());
                            } else if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(eu.mcone.gameapi.api.replay.runner.PlayerRunner.class)) {
                                codec.encode(this);
                            } else if (codecRegistry.getEncoderClass(codec.getCodecID()).equals(eu.mcone.gameapi.replay.player.ReplayPlayer.class)) {
                                codec.encode(player);
                            }
                        }
                    }

                    cTick++;
                }

            } while (cTick > end);

            int newTick = cTick;
            currentTick.set(Math.max(newTick, 0));
        }
    }

    private void sendBlockUpdate(Location location, Material material, byte subID) {
        System.out.println(location.getWorld() != null);
        for (Player player : getWatchers()) {
            player.sendBlockChange(location, material, subID);
        }
    }
}
