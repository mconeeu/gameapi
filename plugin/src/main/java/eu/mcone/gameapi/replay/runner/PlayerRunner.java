package eu.mcone.gameapi.replay.runner;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.npc.NpcAnimationStateChangeEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimplePlayer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityMovePacketContainer;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import eu.mcone.coresystem.api.bukkit.npc.enums.NpcAnimation;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.bukkit.util.BlockSound;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.record.packets.player.EntityDestroyPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.EntitySendChatMessagePacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.EntitySpawnPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityBlockActionPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityBlockBreakPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityBlockPlacePacketContainer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.replay.utils.SoundUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Getter
    private Map<Integer, List<org.bukkit.block.Block>> placedBlocks;
    @Getter
    private Map<Integer, List<org.bukkit.block.Block>> brakedBlocks;

    private ReplaySession session;

    private boolean blockBreak = false;

    @Setter
    @Getter
    private ReplaySpeed replaySpeed = null;
    private int skipped;

    public PlayerRunner(final ReplayPlayer player, final ReplayRunnerManager manager) {
        this.player = player;
        this.manager = manager;
        placedBlocks = new HashMap<>();
        brakedBlocks = new HashMap<>();
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
                Map<Integer, List<PacketContainer>> packets = chunk.getPackets(player.getUuid());

                int repeat;
                if (replaySpeed != null && skipped == replaySpeed.getWait()) {
                    repeat = (replaySpeed.isAdd() ? 2 : 0);
                    skipped = 0;
                } else {
                    repeat = 1;
                }

                if (tick != session.getInfo().getLastTick()) {
                    for (int i = 0; i < repeat; i++) {
                        if (blockBreak) {
                            player.getNpc().sendAnimation(NpcAnimation.SWING_ARM, watcher.toArray(new Player[0]));
                        }

                        if (packets.containsKey(tick)) {
                            for (PacketContainer packet : packets.get(tick)) {
                                Player[] watchers = watcher.toArray(new Player[0]);

                                if (packet instanceof EntitySpawnPacketContainer) {
                                    EntitySpawnPacketContainer spawnPacketContainer = (EntitySpawnPacketContainer) packet;
                                    CoreLocation location = new CoreLocation(spawnPacketContainer.calculateLocation());
                                    player.getNpc().teleport(location);
                                    player.getNpc().togglePlayerVisibility(ListMode.WHITELIST, watchers);
                                    Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.START));
                                    System.out.println("SPAWN");
                                } else if (packet instanceof EntityDestroyPacketContainer) {
                                    CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
                                    Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.END));
                                    System.out.println("DESTROY");
                                }

                                if (packet instanceof EntityMovePacketContainer) {
                                    EntityMovePacketContainer move = (EntityMovePacketContainer) packet;
                                    location = move.calculateLocation();
                                    player.getNpc().teleport(location);

                                    if (tick % 6 == 0) {
                                        SoundUtils.playStepSound(location, watchers);
                                    }

                                    System.out.println("MOVE");
                                }

//                                if (packet instanceof EntitySneakPacketContainer) {
//                                    EntitySneakPacketContainer sneakPacket = (EntitySneakPacketContainer) packet;
//                                    player.getNpc().sneak(sneakPacket.getEntityAction().equals(EntityAction.START_SNEAKING), watchers);
//
//                                    System.out.println("SNEAK");
//                                }

//                                if (packet instanceof EntitySwitchItemPacketContainer) {
//                                    player.getNpc().setItemInHand(((EntitySwitchItemPacketContainer) packet).constructItemStack(), watchers);
//                                    System.out.println("SwitchItem");
//                                }
//
                                if (packet instanceof EntityBlockActionPacketContainer) {
                                    EntityBlockActionPacketContainer entityBlockActionPacketContainer = (EntityBlockActionPacketContainer) packet;

                                    switch (PacketPlayInBlockDig.EnumPlayerDigType.valueOf(entityBlockActionPacketContainer.getAction())) {
                                        case START_DESTROY_BLOCK:
                                            blockBreak = true;
                                            break;
                                        case STOP_DESTROY_BLOCK:
                                            blockBreak = false;
                                            break;
                                    }
                                }
//
//                                if (packet instanceof EntityClickPacketContainer) {
//                                    player.getNpc().sendAnimation(NpcAnimation.SWING_ARM, watchers);
//                                }
//
                                if (packet instanceof EntityBlockPlacePacketContainer) {
                                    EntityBlockPlacePacketContainer place = (EntityBlockPlacePacketContainer) packet;
                                    Location location = place.calculateLocation();
                                    BlockSound sound = new BlockSound(location.getBlock());
                                    sound.playSound(BlockSound.SoundKey.PLACE_SOUND, watchers);
                                    sendBlockUpdate(location, org.bukkit.Material.valueOf(place.getMaterial()), place.getSubID());

                                    if (placedBlocks.containsKey(tick)) {
                                        placedBlocks.get(tick).add(location.getBlock());
                                    } else {
                                        placedBlocks.put(tick, new ArrayList<Block>() {{
                                            add(location.getBlock());
                                        }});
                                    }

                                    blockBreak = false;
                                } else if (packet instanceof EntityBlockBreakPacketContainer) {
                                    EntityBlockBreakPacketContainer breakB = (EntityBlockBreakPacketContainer) packet;
                                    Location location = breakB.calculateLocation();
                                    BlockSound sound = new BlockSound(location.getBlock());
                                    sound.playSound(BlockSound.SoundKey.BREAK_SOUND, watchers);
                                    sendBlockUpdate(location, Material.AIR, (byte) 0);

                                    if (brakedBlocks.containsKey(tick)) {
                                        brakedBlocks.get(tick).add(location.getBlock());
                                    } else {
                                        brakedBlocks.put(tick, new ArrayList<org.bukkit.block.Block>() {{
                                            add(location.getBlock());
                                        }});
                                    }

                                    blockBreak = false;
                                }
//
//                                if (packet instanceof EntityPrimeTntPacketContainer) {
//                                    EntityPrimeTntPacketContainer prime = (EntityPrimeTntPacketContainer) packet;
//                                    Location location = prime.calculateLocation();
//                                    sendBlockUpdate(location, Material.AIR, (byte) 0);
//                                    EntityTNTPrimed tnt = new EntityTNTPrimed(((CraftWorld) location.getWorld()).getHandle());
//                                    tnt.setPosition(location.getX(), location.getY(), location.getZ());
//                                    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(tnt, 50);
//
//                                    for (Player player : watchers) {
//                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnEntity);
//                                    }
//                                }
//
//                                if (packet instanceof EntityDamagePacketContainer) {
//                                    player.getNpc().sendAnimation(NpcAnimation.TAKE_DAMAGE, watchers);
//                                    if (location != null)
//                                        eu.mcone.coresystem.api.bukkit.util.SoundUtils.playSound(EntitySoundKeys.PLAYER_HURT.getNmsSound(), location, watchers);
//                                    System.out.println("DAMAGE");
//                                }
//
//                                if (packet instanceof EntityShootArrowPacketContainer) {
//                                    EntityShootArrowPacketContainer arrowPacket = (EntityShootArrowPacketContainer) packet;
//                                    location.setY(1.5);
//                                    location.getWorld().spawnArrow(location, arrowPacket.getVector().subtract(player.getNpc().getVector()), (float) 3, (float) 0);
//                                    location.getWorld().playSound(location, Sound.ARROW_HIT, 1, 1);
//                                    System.out.println("SHOOT");
//                                }
//
//                                if (packet instanceof EntityLaunchProjectilePacketContainer) {
//                                    EntityLaunchProjectilePacketContainer projectilePacket = (EntityLaunchProjectilePacketContainer) packet;
//                                    player.getNpc().throwProjectile(EntityProjectile.valueOf(projectilePacket.getProjectile()), player.getNpc().getLocation().getDirection());
//                                    System.out.println("LAUNCH");
//                                }
//
//                                if (packet instanceof EntityDeathEventPacketContainer) {
//                                    player.getNpc().togglePlayerVisibility(ListMode.BLACKLIST, watchers);
//                                    System.out.println("DEATH");
//                                }
//
//                                if (packet instanceof EntityRespawnPacketContainer) {
//                                    EntityRespawnPacketContainer respawn = (EntityRespawnPacketContainer) packet;
//                                    player.getNpc().setItemInHand(null);
//                                    player.getNpc().togglePlayerVisibility(ListMode.WHITELIST, watchers);
//                                    player.getNpc().teleport(respawn.calculateLocation());
//                                    System.out.println("RESPAWN");
//                                }
//
//                                if (packet instanceof EntityArmorChangePacketContainer) {
//                                    EntityArmorChangePacketContainer armor = (EntityArmorChangePacketContainer) packet;
//                                    player.getNpc().setEquipment(EquipmentPosition.getPosition(armor.getSlot()), armor.constructItemStack());
//                                    System.out.println("ARMOR");
//                                }
//
//                                if (packet instanceof EntitySendChatMessagePacketContainer) {
//                                    EntitySendChatMessagePacketContainer PacketContainer = (EntitySendChatMessagePacketContainer) packet;
//                                    for (Player player : getWatchers()) {
//                                        player.sendMessage(PacketContainer.getMessage());
//                                    }
//
//                                    System.out.println("CHAT");
//                                }
//
//                                if (packet instanceof EntityPickItemUpPacketContainer) {
//                                    EntityPickItemUpPacketContainer pickup = (EntityPickItemUpPacketContainer) packet;
//
//                                    if (location != null) {
//                                        eu.mcone.coresystem.api.bukkit.util.SoundUtils.playSound(EntitySoundKeys.ENTITY_ITEM_PICKUP.getNmsSound(), location, watchers);
//                                        for (Player player : watchers)
//                                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(pickup.getEntityID()));
//                                    }
//
//                                    System.out.println("PICKITEM");
//                                }
//
//                                if (packet instanceof EntityDropItemPacketContainer) {
//                                    EntityDropItemPacketContainer drop = (EntityDropItemPacketContainer) packet;
//                                    Location location = drop.calculateLocation();
//                                    org.bukkit.inventory.ItemStack item = drop.constructItemStack();
//
//                                    WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
//                                    EntityItem item2 = new EntityItem(nmsWorld);
//                                    //EntityID
//                                    item2.d(drop.getEntityID());
//                                    item2.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
//                                    item2.setItemStack(CraftItemStack.asNMSCopy(item));
//                                    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(item2, 2);
//
//                                    for (Player player : watchers) {
//                                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnEntity);
////                                        player.getWorld().dropItemNaturally(drop.calculateLocation(), item);
//                                    }
//
//
//                                    System.out.println("DROP");
//                                }
//
//                                if (packet instanceof EntityPlayNamedSoundPacketContainer) {
//                                    EntityPlayNamedSoundPacketContainer sound = (EntityPlayNamedSoundPacketContainer) packet;
//                                    eu.mcone.coresystem.api.bukkit.util.SoundUtils.playSound(sound.getId(), sound.calculateLocation(), watchers);
//                                }
//
//                                if (packet instanceof EntityChangeInventoryItemPacketContainer) {
//                                    EntityChangeInventoryItemPacketContainer changeInventoryItem = (EntityChangeInventoryItemPacketContainer) packet;
//                                    player.setInventoryItem(changeInventoryItem.getSlot(), changeInventoryItem.getSerializableItemStack());
//                                }
//
//                                if (packet instanceof EntityChangeInventoryPacketContainer) {
//                                    EntityChangeInventoryPacketContainer inventory = (EntityChangeInventoryPacketContainer) packet;
//                                    player.setInventoryItems(inventory.getItems());
//
//                                    //Update the inventory if the size is bigger than 0
//                                    if (player.getInventoryViewers().size() > 0) {
//                                        for (Map.Entry<Player, CoreInventory> entry : player.getInventoryViewers().entrySet()) {
//                                            for (int slot = 0; slot < 35; slot++) {
//                                                if (entry.getValue().getItems().containsKey(slot)) {
//                                                    org.bukkit.inventory.ItemStack itemStack = entry.getValue().getItems().get(slot).getItemStack();
//                                                    if (!itemStack.getType().equals(org.bukkit.Material.BARRIER)) {
//                                                        entry.getValue().setItem(slot, new ItemStack(Material.AIR));
//                                                    }
//                                                }
//                                            }
//
//                                            for (Map.Entry<Integer, SerializableItemStack> entry1 : inventory.getItems().entrySet()) {
//                                                entry.getValue().setItem(entry1.getKey(), entry1.getValue().constructItemStack());
//                                            }
//
//                                            entry.getKey().updateInventory();
//                                        }
//                                    }
//
//                                    System.out.println("CHANGE INV");
//                                }
//
//                                if (packet instanceof EntityChangeStatePacketContainer) {
//                                    EntityChangeStatePacketContainer entityState = (EntityChangeStatePacketContainer) packet;
//                                    player.setFood(entityState.getFoodLevel());
//                                    player.setHealth(entityState.getHealth());
//                                    System.out.println("CHNAGE HEALTH");
//                                }
//
//                                if (packet instanceof EntityPotionEffectPacketContainer) {
//                                    EntityPotionEffectPacketContainer potionPacket = (EntityPotionEffectPacketContainer) packet;
//                                    for (PotionEffect effect : potionPacket.getPotion().getEffects()) {
//                                        player.getNpc().addPotionEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), watchers);
//                                    }
//
//                                    System.out.println("POTION");
//                                }
//
//                                if (packet instanceof EntityStatsChangePacketContainer) {
//                                    EntityStatsChangePacketContainer stats = (EntityStatsChangePacketContainer) packet;
//                                    player.setStats(new eu.mcone.gameapi.replay.player.ReplayPlayer.Stats(stats.getKills(), stats.getDeaths(), stats.getGoals()));
//                                    System.out.println("STATS");
//                                }
//
//                                if (packet instanceof EntityButtonInteractPacketContainer) {
//                                    BlockStateDirection FACING = BlockStateDirection.of("facing");
//                                    EntityButtonInteractPacketContainer PacketContainer = (EntityButtonInteractPacketContainer) packet;
//                                    Location location = PacketContainer.calculateLocation();
//                                    WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
//                                    BlockPosition blockposition = new BlockPosition(location.getX(), location.getY(), location.getZ());
//                                    IBlockData iblockdata = world.getType(blockposition);
//                                    Block block = iblockdata.getBlock();
//
//                                    block.interact(((CraftWorld) location.getWorld()).getHandle(), blockposition, iblockdata, null, iblockdata.get(FACING), (float) location.getX(), (float) location.getY(), (float) location.getZ());
//                                    System.out.println("BUTTON");
//                                } else if (packet instanceof EntityOpenDoorPacketContainer) {
//                                    EntityOpenDoorPacketContainer PacketContainer = (EntityOpenDoorPacketContainer) packet;
//                                    Location location = PacketContainer.calculateLocation();
//                                    BlockPosition blockposition = new BlockPosition(location.getX(), location.getY(), location.getZ());
//                                    WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
//                                    IBlockData iblockdata = world.getType(blockposition);
//
//                                    BlockStateBoolean OPEN = BlockStateBoolean.of("open");
//                                    BlockStateEnum<BlockDoor.EnumDoorHalf> HALF = BlockStateEnum.of("half", BlockDoor.EnumDoorHalf.class);
//
//                                    BlockPosition blockposition1 = iblockdata.get(HALF) == BlockDoor.EnumDoorHalf.LOWER ? blockposition : blockposition.down();
//                                    IBlockData iblockdata1 = blockposition.equals(blockposition1) ? iblockdata : world.getType(blockposition1);
//
//                                    iblockdata = iblockdata1.a(OPEN);
//                                    world.setTypeAndData(blockposition1, iblockdata, 2);
//                                    world.b(blockposition1, blockposition);
//                                    world.a(iblockdata.get(OPEN) ? 1003 : 1006, blockposition, 0);
//
//                                    if (PacketContainer.isDoorOpen()) {
//                                        location.getWorld().playSound(location, Sound.DOOR_OPEN, 1, 1);
//                                    } else {
//                                        location.getWorld().playSound(location, Sound.DOOR_CLOSE, 1, 1);
//                                    }
//                                }

                                if (forward) {
                                    packetsCount.getAndIncrement();
                                } else {
                                    packetsCount.getAndDecrement();
                                }
                            }

                            skipped++;
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
                    Bukkit.getPluginManager().callEvent(new NpcAnimationStateChangeEvent(player.getNpc(), NpcAnimationStateChangeEvent.NpcAnimationState.END));
                }
            }
        }, 1L, 1L);
    }

    @Override
    public void stop() {
        super.stop();
        CoreSystem.getInstance().getNpcManager().removeNPC(player.getNpc());
        manager.getNpcListener().setUnregister(true);
    }

    public void skip(int skipTicks) {
        if (playing) {
            //Set blocks
            int cTick = currentTick.get();
            int end;

            ReplayChunk chunk;
            Map<Integer, List<PacketContainer>> packets = null;

            if (skipTicks > 0) {
                chunk = manager.getSession().getChunkHandler().getChunk(currentTick.get());
                packets = chunk.getPackets(player.getUuid());
            }

            end = cTick + skipTicks;

            //Check if the end tick is higher then the last tick
            if (end > manager.getSession().getInfo().getLastTick()) {
                end = manager.getSession().getInfo().getLastTick();
            } else if (end < 0) {
                end = 0;
            }

            while (true) {
                if (packets == null) {
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
                    if (packets.containsKey(cTick)) {
                        for (PacketContainer packet : packets.get(cTick)) {
                            if (packet instanceof EntityBlockPlacePacketContainer) {
                                EntityBlockPlacePacketContainer placeBlock = (EntityBlockPlacePacketContainer) packet;
                                sendBlockUpdate(placeBlock.calculateLocation(), org.bukkit.Material.valueOf(placeBlock.getMaterial()), placeBlock.getSubID());
                            } else if (packet instanceof EntityBlockBreakPacketContainer) {
                                EntityBlockBreakPacketContainer breakBlock = (EntityBlockBreakPacketContainer) packet;
                                sendBlockUpdate(breakBlock.calculateLocation(), org.bukkit.Material.AIR, (byte) 0);
                            }

                            if (packet instanceof EntitySendChatMessagePacketContainer) {
                                EntitySendChatMessagePacketContainer PacketContainer = (EntitySendChatMessagePacketContainer) packet;
                                for (Player player : getWatchers()) {
                                    player.sendMessage(PacketContainer.getMessage());
                                }
                            }
                        }
                    }

                    cTick++;
                }

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

    private void sendBlockUpdate(Location location, Material material, byte subID) {
        System.out.println(location.getWorld() != null);
        for (Player player : getWatchers()) {
            player.sendBlockChange(location, material, subID);
        }
    }
}
