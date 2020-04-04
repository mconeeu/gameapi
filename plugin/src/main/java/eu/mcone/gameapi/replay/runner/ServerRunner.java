package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.record.packets.server.EntityTntExplodePacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerBroadcastMessagePacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableBlock;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerRunner extends SimplePlayer {

    @Setter
    @Getter
    private ReplaySpeed replaySpeed = null;
    private int skipped;

    private ReplayRunnerManager manager;
    private Map<String, List<PacketWrapper>> packets;

    public ServerRunner(final ReplayRunnerManager manager) {
        this.manager = manager;
        packets = manager.getSession().getMessages();
    }

    @Override
    public void play() {
        currentTick = new AtomicInteger(0);
        AtomicInteger packetsCount = new AtomicInteger(0);

        playingTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getInstance(), () -> {
            if (playing) {
                int tick = currentTick.get();
                String sTick = String.valueOf(tick);

                ReplayChunk chunk = manager.getSession().getChunkHandler().getChunk(tick);
                List<PacketWrapper> serverPackets = chunk.getServerPackets(tick);

                int repeat;
                if (replaySpeed != null && skipped == replaySpeed.getWait()) {
                    repeat = (replaySpeed.isAdd() ? 2 : 0);
                    skipped = 0;
                } else {
                    repeat = 1;
                }

                if (tick != manager.getSession().getInfo().getLastTick()) {
                    for (int i = 0; i < repeat; i++) {
                        if (packets.containsKey(sTick)) {
                            for (PacketWrapper packet : packets.get(sTick)) {
                                if (packet instanceof ServerBroadcastMessagePacketWrapper) {
                                    ServerBroadcastMessagePacketWrapper broadcast = (ServerBroadcastMessagePacketWrapper) packet;
                                    Bukkit.broadcastMessage(broadcast.getMessage().getMessage());
                                }
                            }

                            skipped++;
                        }

                        if (serverPackets != null) {
                            for (PacketWrapper packet : serverPackets) {
                                if (packet instanceof EntityTntExplodePacketWrapper) {
                                    EntityTntExplodePacketWrapper tntExplodePacketWrapper = (EntityTntExplodePacketWrapper) packet;
                                    sendBlockUpdate(tntExplodePacketWrapper.calculateLocation(), Material.AIR, (byte) 0);

                                    for (Player player : watcher) {
                                        player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
                                    }

                                    for (SerializableBlock block : tntExplodePacketWrapper.getDestroy()) {
                                        sendBlockUpdate(block.getLocation(), Material.AIR, (byte) 0);
                                    }
                                }
                            }

                            skipped++;
                        }

                        if (forward) {
                            packetsCount.getAndIncrement();
                        } else {
                            packetsCount.getAndDecrement();
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
                    playingTask.cancel();
                }
            }

        }, 1L, 1L);
    }

    private void sendBlockUpdate(Location location, Material material, byte ID) {
        for (Player player : getWatchers()) {
            player.sendBlockChange(location, material, ID);
        }
    }

    public void skip(int skipTicks) {
        if (playing) {
            int cTick = currentTick.get();
            int end;
            end = cTick + skipTicks;

            //Check if the end tick is higher then the last tick
            if (end > manager.getSession().getInfo().getLastTick()) {
                end = manager.getSession().getInfo().getLastTick();
            } else if (end < 0) {
                end = 0;
            }

            while (true) {
                String sTick = String.valueOf(cTick);
                if (packets.containsKey(sTick)) {
                    for (PacketWrapper packet : packets.get(sTick)) {
                        if (packet instanceof ServerBroadcastMessagePacketWrapper) {
                            ServerBroadcastMessagePacketWrapper broadcast = (ServerBroadcastMessagePacketWrapper) packet;
                            Bukkit.broadcastMessage(broadcast.getMessage().getMessage());
                        }
                    }
                }

                cTick++;

                if (cTick <= end) {
                    break;
                }
            }

            int newTick = cTick;
            if (newTick > 0) {
                currentTick.set(newTick);
            } else {
                currentTick.set(0);
            }
        }
    }
}
