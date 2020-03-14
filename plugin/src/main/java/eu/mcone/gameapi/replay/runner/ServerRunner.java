package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerBroadcastMessagePacketWrapper;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerRunner extends SimplePlayer {

    private Map<String, List<PacketWrapper>> packets;

    public ServerRunner(final ReplaySession replaySession) {
        packets = replaySession.getMessages();
    }

    @Override
    public void play() {
        currentTick = new AtomicInteger(0);
        AtomicInteger packetsCount = new AtomicInteger(0);

        playingTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getInstance(), () -> {
            if (playing) {
                String tick = String.valueOf(currentTick.get());
                System.out.println("SERVER TICK " + tick);
                if (packetsCount.get() < packets.size() - 1) {
                    if (packets.containsKey(tick)) {
                        for (PacketWrapper packet : packets.get(tick)) {
                            if (packet instanceof ServerBroadcastMessagePacketWrapper) {
                                ServerBroadcastMessagePacketWrapper broadcast = (ServerBroadcastMessagePacketWrapper) packet;
                                Bukkit.broadcastMessage(broadcast.getMessage().getMessage());
                            }

                            packetsCount.getAndIncrement();
                        }
                    }

                    if (forward) {
                        this.currentTick.getAndIncrement();
                        packetsCount.getAndIncrement();
                    } else if (backward) {
                        if (Integer.valueOf(tick) >= 1) {
                            currentTick.getAndDecrement();
                            packetsCount.getAndDecrement();
                        } else {
                            forward = true;
                            currentTick.getAndIncrement();
                            packetsCount.getAndIncrement();
                        }
                    }
                } else {
                    playing = false;
                    playingTask.cancel();
                }
            }
        }, (long) (1L * speed), (long) (1L * speed));
    }
}
