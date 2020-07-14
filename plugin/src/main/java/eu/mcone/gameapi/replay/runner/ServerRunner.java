package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.replay.container.ReplayContainer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerRunner extends eu.mcone.coresystem.api.bukkit.npc.capture.Player implements eu.mcone.gameapi.api.replay.runner.ServerRunner {

    @Setter
    @Getter
    private ReplaySpeed replaySpeed = null;

    private final ReplayContainer container;
    private final ScheduledThreadPoolExecutor executor;

    public ServerRunner(final ReplayContainer container) {
        this.container = container;
        this.executor = (ScheduledThreadPoolExecutor) Executors.newSingleThreadExecutor();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void play() {
        if (!playing) {
            currentTick.set(0);
        }

        executor.schedule(() -> {
            if (playing) {
                int tick = currentTick.get();
                if (tick != container.getReplay().getLastTick()) {
                    String sTick = String.valueOf(tick);
                    ReplayChunk chunk = container.getChunkHandler().getChunk(tick);
                    List<Codec<?, ?>> codecs = chunk.getServerCodecs(tick);
                    if (codecs != null) {
                        for (Codec codec : codecs) {
                            if (codec.getEncodeClass().equals(eu.mcone.gameapi.api.replay.runner.ServerRunner.class)) {
                                codec.encode(this);
                            }
                        }
                    }

                    if (container.getReplay().getMessages().containsKey(sTick)) {
                        for (MessageWrapper wrapper : container.getReplay().getMessages().get(sTick)) {
                            for (Player watcher : getWatchers()) {
                                watcher.sendMessage(wrapper.getMessage());
                            }
                        }
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
                }
            }
        }, replaySpeed.getSpeed(), TimeUnit.MILLISECONDS);

        if (playing) {
            play();
        }
    }

    private void sendBlockUpdate(Location location, Material material, byte ID) {
        for (Player player : getWatchers()) {
            player.sendBlockChange(location, material, ID);
        }
    }

    public void skip(int skipTicks) {
        if (playing) {
            int cTick = currentTick.get();
            int end = cTick + skipTicks;

            //Check if the end tick is higher then the last tick
            if (end > container.getReplay().getLastTick()) {
                end = container.getReplay().getLastTick();
            } else if (end < 0) {
                end = 0;
            }

            do {
                String sTick = String.valueOf(cTick);
                if (container.getReplay().getMessages().containsKey(sTick)) {
                    for (MessageWrapper wrapper : container.getReplay().getMessages().get(sTick)) {
                        for (Player watcher : getWatchers()) {
                            watcher.sendMessage(wrapper.getMessage());
                        }
                    }
                }

                cTick++;
            } while (cTick > end);

            int newTick = cTick;
            currentTick.set(Math.max(newTick, 0));
        }
    }
}
