package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.npc.enums.EquipmentPosition;
import eu.mcone.coresystem.api.bukkit.npc.enums.NpcAnimation;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.record.packets.player.*;
import eu.mcone.gameapi.replay.player.ReplayPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Replay extends SimplePlayer implements Listener, eu.mcone.gameapi.api.replay.utils.Replay {

    @Getter
    private final ReplayPlayer player;
    @Getter
    private PlayerNpc npc;
    @Getter
    private final Map<String, List<PacketWrapper>> packets;
    @Getter
    private Location location;

    public Replay(final ReplayPlayer player) {
        this.player = player;
        this.packets = player.getPackets();
    }

    @Override
    public void play() {
        currentTick = new AtomicInteger(0);
        AtomicInteger packetsCount = new AtomicInteger(0);
        AtomicInteger currentProgress = new AtomicInteger(0);
        System.out.println("--Play replay");
        playingTask = Bukkit.getScheduler().runTaskTimer(GameAPIPlugin.getInstance(), () -> {
                    System.out.println("DEBUG-1");
                    if (playing) {
                        System.out.println("PLAYING");
                        String tick = String.valueOf(currentTick.get());
                        if (packetsCount.get() < packets.size() - 1) {
                            if (packets.containsKey(tick)) {
                                System.out.println("PACKET");

                                for (PacketWrapper packet : packets.get(tick)) {
                                    Player[] watchers = watcher.toArray(new Player[0]);

                                    if (packet instanceof EntitySpawnPacketWrapper) {
                                        EntitySpawnPacketWrapper spawnPacketWrapper = (EntitySpawnPacketWrapper) packet;
                                        System.out.println(spawnPacketWrapper.getLocation().toString());
                                        npc = GamePlugin.getPlugin().getSessionManager().getNpcManager().createNpc(player, new CoreLocation(spawnPacketWrapper.getLocation()));
                                        Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(npc, NpcAnimationStateChangeEvent.NpcAnimationState.START));
                                    }

                                    if (packet instanceof EntityDestroyPacketWrapper) {
                                        CoreSystem.getInstance().getNpcManager().removeNPC(npc);
                                        Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(npc, NpcAnimationStateChangeEvent.NpcAnimationState.END));
                                    }

                                    if (packet instanceof EntityMovePacketWrapper) {
                                        EntityMovePacketWrapper move = (EntityMovePacketWrapper) packet;
                                        location = move.calculateLocation();
                                        npc.getData().setLocation(new CoreLocation(location));
                                        npc.teleport(location);
                                    }

                                    if (packet instanceof EntitySneakPacketWrapper) {
                                        EntitySneakPacketWrapper sneakPacket = (EntitySneakPacketWrapper) packet;
                                        if (sneakPacket.getEntityAction().equals(EntityAction.START_SNEAKING)) {
                                            npc.sneak(true, watchers);
                                        } else {
                                            npc.sneak(false, watchers);
                                        }
                                    }

                                    if (packet instanceof EntitySwitchItemPacketWrapper) {
                                        npc.setItemInHand(((EntitySwitchItemPacketWrapper) packet).constructItemStack(), watchers);
                                    }

                                    if (packet instanceof EntityClickPacketWrapper) {
                                        npc.sendAnimation(NpcAnimation.SWING_ARM, watchers);
                                    }

                                    if (packet instanceof EntityButtonInteractPacketWrapper) {
                                        BlockStateDirection FACING = BlockStateDirection.of("facing");
                                        EntityButtonInteractPacketWrapper packetWrapper = (EntityButtonInteractPacketWrapper) packet;
                                        Location location = packetWrapper.calculateLocation();
                                        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
                                        BlockPosition blockposition = new BlockPosition(location.getX(), location.getY(), location.getZ());
                                        IBlockData iblockdata = world.getType(blockposition);
                                        Block block = iblockdata.getBlock();

                                        block.interact(((CraftWorld) location.getWorld()).getHandle(), blockposition, iblockdata, null, iblockdata.get(FACING), (float) location.getX(), (float) location.getY(), (float) location.getZ());
                                    } else if (packet instanceof EntityOpenDoorPacketWrapper) {
                                        EntityOpenDoorPacketWrapper packetWrapper = (EntityOpenDoorPacketWrapper) packet;
                                        Location location = packetWrapper.calculateLocation();
                                        BlockPosition blockposition = new BlockPosition(location.getX(), location.getY(), location.getZ());
                                        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
                                        IBlockData iblockdata = world.getType(blockposition);

                                        BlockStateBoolean OPEN = BlockStateBoolean.of("open");
                                        BlockStateEnum<BlockDoor.EnumDoorHalf> HALF = BlockStateEnum.of("half", BlockDoor.EnumDoorHalf.class);

                                        BlockPosition blockposition1 = iblockdata.get(HALF) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.down();
                                        IBlockData iblockdata1 = blockposition.equals(blockposition1) ? iblockdata : world.getType(blockposition1);

                                        iblockdata = iblockdata1.a(OPEN);
                                        world.setTypeAndData(blockposition1, iblockdata, 2);
                                        world.b(blockposition1, blockposition);
                                        world.a(iblockdata.get(OPEN) ? 1003 : 1006, blockposition, 0);

                                        if (packetWrapper.isDoorOpen()) {
                                            location.getWorld().playSound(location, Sound.DOOR_OPEN, 1, 1);
                                        } else {
                                            location.getWorld().playSound(location, Sound.DOOR_CLOSE, 1, 1);
                                        }
                                    }

                                    if (packet instanceof EntityPotionEffectPacketWrapper) {
                                        EntityPotionEffectPacketWrapper potionPacket = (EntityPotionEffectPacketWrapper) packet;
                                        for (PotionEffect effect : potionPacket.getPotion().getEffects()) {
                                            npc.addPotionEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), watchers);
                                        }
                                    }

                                    if (packet instanceof EntityDamagePacketWrapper) {
                                        npc.sendAnimation(NpcAnimation.TAKE_DAMAGE, watchers);
                                    }

                                    if (packet instanceof EntitySendChatMessagePacketWrapper) {
                                        EntitySendChatMessagePacketWrapper packetWrapper = (EntitySendChatMessagePacketWrapper) packet;
                                        for (Player player : getWatchers()) {
                                            player.sendMessage(packetWrapper.getChatMessage().getMessage());
                                        }
                                    }

                                    if (packet instanceof EntityArmorChangePacketWrapper) {
                                        EntityArmorChangePacketWrapper armor = (EntityArmorChangePacketWrapper) packet;
                                        //Rest armor map
                                        ((PlayerNpcData) npc.getEntityData()).setEquipment(new HashMap<>());

                                        int pos = 0;
                                        for (ItemStack itemStack : armor.constructItemStackArray()) {
                                            npc.setEquipment(EquipmentPosition.getPosition(pos), itemStack);
                                            pos++;
                                        }
                                    }

                                    if (packet instanceof EntityDropItemPacketWrapper) {
                                        EntityDropItemPacketWrapper drop = (EntityDropItemPacketWrapper) packet;
                                        ItemStack item = drop.constructItemStack();
                                        for (Player player : watchers) {
                                            player.getWorld().dropItemNaturally(location, item);
                                        }
                                    }

                                    if (packet instanceof EntityPickItemUpPacketWrapper) {
                                        EntityPickItemUpPacketWrapper pickup = (EntityPickItemUpPacketWrapper) packet;

                                        for (Entity entity : location.getWorld().getEntities()) {
                                            System.out.println(entity.getName());
                                            if (entity.getName().equalsIgnoreCase(pickup.getItem())) {
                                                entity.remove();
                                            }
                                        }
                                    }

                                    if (packet instanceof EntityChangeInventoryPacketWrapper) {
                                        EntityChangeInventoryPacketWrapper inventory = (EntityChangeInventoryPacketWrapper) packet;
                                        player.setInventoryContent(inventory.constructItemStackArray());

                                        //Update the inventory if the size is bigger than 0
                                        if (player.getViewInventory().size() > 0) {
                                            for (Player player : player.getViewInventory()) {
                                                player.updateInventory();
                                            }
                                        }
                                    }

                                    if (packet instanceof EntityChangeHealthPacketWrapper) {
                                        EntityChangeHealthPacketWrapper health = (EntityChangeHealthPacketWrapper) packet;
                                        player.setHealth(health.getHealth());
                                    }

                                    if (packet instanceof EntityStatsChangePacketWrapper) {
                                        EntityStatsChangePacketWrapper stats = (EntityStatsChangePacketWrapper) packet;
                                        player.setStats(new ReplayPlayer.Stats(stats.getKills(), stats.getDeaths(), stats.getGoals()));
                                    }
                                }

                                packetsCount.getAndIncrement();
                            }

                            if (forward) {
                                this.currentTick.getAndIncrement();
                                System.out.println("INCREMENT");
                            } else if (backward) {
                                System.out.println("DECREMENT");
                                this.currentTick.getAndDecrement();
                            }
                        } else {
                            playing = false;
                            playingTask.cancel();
                            Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(npc, NpcAnimationStateChangeEvent.NpcAnimationState.END));
                        }
                    }
                },
                1L, 1L
//                (long) (1L * speed), (long) (1L * speed)
        );
    }
}
