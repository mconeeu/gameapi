package eu.mcone.gameapi.api.utils;

import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.replay.record.packets.player.EntityBreakBlockPacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.player.EntityPlaceBlockPacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.world.WorldPlayEffectPacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.world.WorldPlaySoundPacketWrapper;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplayWorld extends SimplePlayer {

    private ReplaySession session;
    private final HashMap<String, List<PacketWrapper>> packets;

    public ReplayWorld(final ReplaySession replaySession) {
        session = replaySession;
        packets = replaySession.getWorldPackets();
    }

    @Override
    public void play() {
        currentTick = new AtomicInteger(0);
        AtomicInteger packetsCount = new AtomicInteger(0);
        AtomicInteger currentProgress = new AtomicInteger(0);

        playingTask = Bukkit.getScheduler().runTaskTimer(GameAPI.getInstance(), () -> {
            if (playing) {
                String tick = String.valueOf(currentTick.get());
                System.out.println("WORLD TICK: " + tick + "//" + packets.size());
                if (packetsCount.get() < packets.size() - 1) {
                    if (packets.containsKey(tick)) {
                        for (PacketWrapper packet : packets.get(tick)) {
                            Player[] watchers = watcher.toArray(new Player[0]);

                            if (packet instanceof EntityPlaceBlockPacketWrapper) {
                                EntityPlaceBlockPacketWrapper place = (EntityPlaceBlockPacketWrapper) packet;
                                for (Player player : watchers) {
                                    player.sendBlockChange(place.getLocation(), place.getMaterial(), (byte) place.getMaterial().getId());
                                }
                            } else if (packet instanceof EntityBreakBlockPacketWrapper) {
                                EntityBreakBlockPacketWrapper packetWrapper = (EntityBreakBlockPacketWrapper) packet;
                                for (Player player : watchers) {
                                    player.sendBlockChange(packetWrapper.getLocation(), Material.AIR, (byte) 0);
                                }
                            }

                            if (packet instanceof WorldPlayEffectPacketWrapper) {
                                WorldPlayEffectPacketWrapper effect = (WorldPlayEffectPacketWrapper) packet;
                                for (Player player : watcher) {
                                    effect.spawnEffect(player);
                                }
                            }

                            if (packet instanceof WorldPlaySoundPacketWrapper) {
                                WorldPlaySoundPacketWrapper sound = (WorldPlaySoundPacketWrapper) packet;
                                for (Player player : watcher) {
                                    sound.playSound(player);
                                }
                            }

                            packetsCount.getAndIncrement();
                        }
                    }

                    if (forward) {
                        this.currentTick.getAndIncrement();
                    } else if (backward) {
                        this.currentTick.getAndDecrement();
                    }
                } else {
                    playing = false;
                    playingTask.cancel();
                }
            }
        }, (long) (1L * speed), (long) (1L * speed));
    }
}
