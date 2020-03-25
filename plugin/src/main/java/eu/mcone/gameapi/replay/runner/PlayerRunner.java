package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile;
import eu.mcone.coresystem.api.bukkit.npc.enums.EquipmentPosition;
import eu.mcone.coresystem.api.bukkit.npc.enums.NpcAnimation;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.bukkit.util.BlockSound;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.record.packets.player.*;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.replay.utils.SoundUtils;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerRunner extends SimplePlayer implements eu.mcone.gameapi.api.replay.runner.PlayerRunner {
    @Getter
    private final ReplayPlayer player;
    @Getter
    private final ReplayRunnerManager manager;
    @Getter
    private Location location;

    private ReplaySession session;

    public PlayerRunner(final ReplayPlayer player, final ReplayRunnerManager manager) {
        this.player = player;
        this.manager = manager;
        this.session = GamePlugin.getGamePlugin().getSessionManager().getSession(player.getData().getSessionID());
    }

    @Override
    public void play() {
        currentTick = new AtomicInteger(0);
        AtomicInteger packetsCount = new AtomicInteger(0);

        playingTask = Bukkit.getScheduler().runTaskTimer(GameAPIPlugin.getInstance(), () -> {
            if (playing) {
                int tick = currentTick.get();
                ReplayChunk chunk = manager.getSession().getChunkHandler().getChunk(tick);
                Map<Integer, List<PacketWrapper>> packets = chunk.getPackets(player.getUuid());

                if (tick != session.getInfo().getLastTick()) {
                    if (packets.containsKey(tick)) {
                        for (PacketWrapper packet : packets.get(tick)) {
                            Player[] watchers = watcher.toArray(new Player[0]);

                            if (packet instanceof EntitySpawnPacketWrapper) {
                                EntitySpawnPacketWrapper spawnPacketWrapper = (EntitySpawnPacketWrapper) packet;
                                CoreLocation location = new CoreLocation(spawnPacketWrapper.calculateLocation());
                                player.getNpc().teleport(location);
                                //player.getNpc().togglePlayerVisibility(ListMode.BLACKLIST);
                                Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.START));
                            } else if (packet instanceof EntityDestroyPacketWrapper) {
                                CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
                                Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.END));
                            }

                            if (packet instanceof EntityMovePacketWrapper) {
                                EntityMovePacketWrapper move = (EntityMovePacketWrapper) packet;
                                location = move.calculateLocation();
                                player.getNpc().teleport(location);

                                if (tick % 6 == 0)
                                    SoundUtils.playStepSound(location, watchers);
                            }

                            if (packet instanceof EntitySneakPacketWrapper) {
                                EntitySneakPacketWrapper sneakPacket = (EntitySneakPacketWrapper) packet;
                                if (sneakPacket.getEntityAction().equals(EntityAction.START_SNEAKING)) {
                                    player.getNpc().sneak(true, watchers);
                                } else {
                                    player.getNpc().sneak(false, watchers);
                                }
                            }

                            if (packet instanceof EntitySwitchItemPacketWrapper) {
                                player.getNpc().setItemInHand(((EntitySwitchItemPacketWrapper) packet).constructItemStack(), watchers);
                            }

                            if (packet instanceof EntityClickPacketWrapper) {
                                player.getNpc().sendAnimation(NpcAnimation.SWING_ARM, watchers);
                            }

                            if (packet instanceof EntityPlaceBlockPacketWrapper) {
                                EntityPlaceBlockPacketWrapper place = (EntityPlaceBlockPacketWrapper) packet;
                                Location location = place.calculateLocation();
                                BlockSound sound = new BlockSound(location.getBlock());
                                sound.playSound(BlockSound.SoundKey.BREAK_SOUND, watchers);

                                for (Player player : watchers) {
                                    player.sendBlockChange(location, org.bukkit.Material.valueOf(place.getMaterial()), (byte) CraftMagicNumbers.getBlock(org.bukkit.Material.valueOf(place.getMaterial())).k());
                                }
                            } else if (packet instanceof EntityBreakBlockPacketWrapper) {
                                EntityBreakBlockPacketWrapper breakB = (EntityBreakBlockPacketWrapper) packet;
                                Location location = breakB.calculateLocation();
                                BlockSound sound = new BlockSound(location.getBlock());
                                sound.playSound(BlockSound.SoundKey.BREAK_SOUND, watchers);

                                for (Player player : watchers) {
                                    player.sendBlockChange(location, org.bukkit.Material.AIR, (byte) CraftMagicNumbers.getBlock(org.bukkit.Material.AIR).k());

                                }
                            }

                            if (packet instanceof EntityDamagePacketWrapper) {
                                player.getNpc().sendAnimation(NpcAnimation.TAKE_DAMAGE, watchers);
                            }

                            if (packet instanceof EntityShootArrowPacketWrapper) {
                                EntityShootArrowPacketWrapper arrowPacket = (EntityShootArrowPacketWrapper) packet;
                                location.getWorld().spawnArrow(location, arrowPacket.getVector().subtract(player.getNpc().getVector()), (float) 3, (float) 0);
                            }

                            if (packet instanceof EntityLaunchProjectilePacketWrapper) {
                                EntityLaunchProjectilePacketWrapper projectilePacket = (EntityLaunchProjectilePacketWrapper) packet;
                                player.getNpc().throwProjectile(EntityProjectile.valueOf(projectilePacket.getProjectile()));
                            }

                            if (packet instanceof EntityDeathEventPacketWrapper) {
                                player.getNpc().togglePlayerVisibility(ListMode.BLACKLIST);
                            }

                            if (packet instanceof EntityRespawnPacketWrapper) {
                                EntityRespawnPacketWrapper respawn = (EntityRespawnPacketWrapper) packet;
                                player.getNpc().setItemInHand(null);
                                player.getNpc().togglePlayerVisibility(ListMode.WHITELIST);
                                player.getNpc().teleport(respawn.calculateLocation());
                            }

                            if (packet instanceof EntityArmorChangePacketWrapper) {
                                EntityArmorChangePacketWrapper armor = (EntityArmorChangePacketWrapper) packet;
                                player.getNpc().setEquipment(EquipmentPosition.getPosition(armor.getSlot()), armor.constructItemStack());
                            }

                            if (packet instanceof EntitySendChatMessagePacketWrapper) {
                                EntitySendChatMessagePacketWrapper packetWrapper = (EntitySendChatMessagePacketWrapper) packet;
                                for (Player player : getWatchers()) {
                                    player.sendMessage(packetWrapper.getMessage());
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

                            if (packet instanceof EntityDropItemPacketWrapper) {
                                EntityDropItemPacketWrapper drop = (EntityDropItemPacketWrapper) packet;
                                org.bukkit.inventory.ItemStack item = drop.constructItemStack();
                                for (Player player : watchers) {
                                    player.getWorld().dropItemNaturally(location, item);
                                }
                            }

                            if (packet instanceof EntityChangeInventoryPacketWrapper) {
                                EntityChangeInventoryPacketWrapper inventory = (EntityChangeInventoryPacketWrapper) packet;
                                player.setInventoryItems(inventory.getItems());

                                //Update the inventory if the size is bigger than 0
                                if (player.getInventoryViewers().size() > 0) {
                                    for (Map.Entry<Player, CoreInventory> entry : player.getInventoryViewers().entrySet()) {
                                        for (int i = 0; i < 35; i++) {
                                            if (entry.getValue().getItems().containsKey(i)) {
                                                org.bukkit.inventory.ItemStack itemStack = entry.getValue().getItems().get(i).getItemStack();
                                                if (!itemStack.getType().equals(org.bukkit.Material.BARRIER)) {
                                                    entry.getValue().setItem(i, new ItemStack(Material.AIR));
                                                }
                                            }
                                        }

                                        for (Map.Entry<Integer, SerializableItemStack> entry1 : inventory.getItems().entrySet()) {
                                            entry.getValue().setItem(entry1.getKey(), entry1.getValue().constructItemStack());
                                        }

                                        entry.getKey().updateInventory();
                                    }
                                }
                            }

                            if (packet instanceof EntityChangeHealthPacketWrapper) {
                                EntityChangeHealthPacketWrapper health = (EntityChangeHealthPacketWrapper) packet;
                                player.setHealth(health.getHealth());
                            }

                            if (packet instanceof EntityPotionEffectPacketWrapper) {
                                EntityPotionEffectPacketWrapper potionPacket = (EntityPotionEffectPacketWrapper) packet;
                                for (PotionEffect effect : potionPacket.getPotion().getEffects()) {
                                    player.getNpc().addPotionEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), watchers);
                                }
                            }

                            if (packet instanceof EntityStatsChangePacketWrapper) {
                                EntityStatsChangePacketWrapper stats = (EntityStatsChangePacketWrapper) packet;
                                player.setStats(new eu.mcone.gameapi.replay.player.ReplayPlayer.Stats(stats.getKills(), stats.getDeaths(), stats.getGoals()));
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
                        }
                    }

                    if (forward) {
                        this.currentTick.getAndIncrement();
                        packetsCount.getAndIncrement();
                    } else if (backward) {
                        if (tick >= 1) {
                            currentTick.getAndDecrement();
                            packetsCount.getAndDecrement();
                        } else {
                            forward = true;
                            currentTick.getAndIncrement();
                            packetsCount.getAndIncrement();
                        }
                    }
                } else {
                    System.out.println("STOP PLAYING");
                    playing = false;
                    playingTask.cancel();
                    Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.END));
                }
            }
        }, (long) (1L * speed), (long) (1L * speed));
    }

    @Override
    public void stop() {
        super.stop();
        CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
        manager.getNpcListener().setUnregister(true);
    }
}
