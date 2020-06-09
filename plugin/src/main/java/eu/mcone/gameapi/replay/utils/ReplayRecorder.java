package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.BroadcastMessageEvent;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimpleRecorder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.coresystem.api.bukkit.util.PacketListener;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplaySessionEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplaySessionEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.record.packets.player.*;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityBlockBreakPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityBlockPlacePacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.block.EntityPrimeTntPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.inventory.EntityChangeInventoryItemPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.player.inventory.EntityChangeInventoryPacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.server.EntityTntExplodePacketContainer;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerBroadcastMessagePacketContainer;
import eu.mcone.gameapi.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.replay.session.ReplaySession;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.material.Button;
import org.bukkit.material.Door;
import org.bukkit.potion.Potion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReplayRecorder extends SimpleRecorder implements eu.mcone.gameapi.api.utils.ReplayRecorder {

    private final ReplaySession session;
    @Getter
    private final Map<Integer, eu.mcone.gameapi.api.replay.chunk.ReplayChunk> chunks;
    private int lastTick;

    public ReplayRecorder(final ReplaySession session) {
        this.session = session;
        chunks = new HashMap<>();
    }

    @Override
    public void record() {
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(CoreSystem.getInstance(), () -> ticks++, 1L, 1L);

        CoreSystem.getInstance().getPacketManager().registerPacketListener(new PacketListener() {
            @Override
            public void onPacketIn(Player player, Packet<?> packet) {
                if (isStopped) {
                    CoreSystem.getInstance().getPacketManager().unregisterPacketListener(this);
                } else {
//                    if (packet instanceof PacketPlayInBlockDig) {
//                        PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig) packet;
//                        BlockPosition position = packetPlayInBlockDig.a();
//                        PacketPlayInBlockDig.EnumPlayerDigType type = packetPlayInBlockDig.c();
//
//                        System.out.println("DIG");
//                        addPacket(player, new EntityBlockActionPacketContainer(type));
//
//                        if (type.equals(PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK)) {
//                            addPacket(player, new EntityBlockBreakPacketContainer(position, player.getLocation().getWorld().getName()));
//                        }
//                    }

//                    if (packet instanceof PacketPlayInBlockPlace) {
//                        PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace) packet;
//                        ItemStack itemStack = CraftItemStack.asBukkitCopy(packetPlayInBlockPlace.getItemStack());
//                        BlockPosition position = (BlockPosition) ReflectionManager.getValue(packetPlayInBlockPlace, "b");
//                        addPacket(player, new EntityBlockPlacePacketContainer(itemStack, position, player.getLocation().getWorld().getName()));
//                        System.out.println("PLACE");
//                    }

                    if (packet instanceof PacketPlayInHeldItemSlot) {
//                        PacketPlayInHeldItemSlot packetPlayInHeldItemSlot = (PacketPlayInHeldItemSlot) packet;
//                        int slot = (int) ReflectionManager.getValue(packetPlayInHeldItemSlot, "itemInHandIndex");
                        addPacket(player, new EntitySwitchItemPacketContainer(player.getItemInHand()));
                    }

                    if (packet instanceof PacketPlayInEntityAction) {
                        PacketPlayInEntityAction packetPlayInEntityAction = (PacketPlayInEntityAction) packet;
                        PacketPlayInEntityAction.EnumPlayerAction action = (PacketPlayInEntityAction.EnumPlayerAction) ReflectionManager.getValue(packetPlayInEntityAction, "animation");

                        switch (action) {
                            case START_SNEAKING:
                                addPacket(player, new EntitySneakPacketContainer(EntityAction.START_SNEAKING));
                                break;
                            case STOP_SNEAKING:
                                addPacket(player, new EntitySneakPacketContainer(EntityAction.STOP_SNEAKING));
                                break;
                        }
                    }
//                    if (packet instanceof PacketPlayOutEntityStatus) {
//                        PacketPlayOutEntityStatus playOutEntityStatus = (PacketPlayOutEntityStatus) packet;
//                        byte ID = (byte) ReflectionManager.getValue(playOutEntityStatus, "b");
//
//                        if (ID == 2) {
//                            addPacket(player, new EntityDeathEventPacketContainer());
//                        }
//                    }
                }
            }

            @Override
            public void onPacketOut(Player player, Packet<?> packet) {
                EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

                if (isStopped) {
                    CoreSystem.getInstance().getPacketManager().unregisterPacketListener(this);
                } else {
//                    if (packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook) {
//                        if (ticks % 2 == 0) {
//                            PacketPlayOutEntity moveLook = (PacketPlayOutEntity) packet;
//                            byte x = (byte) ReflectionManager.getValue(moveLook, "b");
//                            byte y = (byte) ReflectionManager.getValue(moveLook, "c");
//                            byte z = (byte) ReflectionManager.getValue(moveLook, "d");
//                            byte yaw = (byte) ReflectionManager.getValue(moveLook, "e");
//                            byte pitch = (byte) ReflectionManager.getValue(moveLook, "f");
//
//                            addPacket(player, new EntityMovePacketContainer((double) x / 32.0D, (double) y / 32.0D, (double) z / 32.0D, (float) yaw / 256.0F * 360.0F, (float) pitch / 256.0F * 360.0F, player.getLocation().getWorld().getName()));
//                        }
//                    }

                    if (packet instanceof PacketPlayOutUpdateHealth) {
                        PacketPlayOutUpdateHealth packetPlayOutUpdateHealth = (PacketPlayOutUpdateHealth) packet;
                        int food = (int) ReflectionManager.getValue(packetPlayOutUpdateHealth, "b");
                        float health = (float) ReflectionManager.getValue(packetPlayOutUpdateHealth, "a");

                        addPacket(player, new EntityChangeStatePacketContainer(food, (int) health));
                    }

                    //Inventory
                    if (packet instanceof PacketPlayOutWindowItems) {
                        PacketPlayOutWindowItems packetPlayOutWindowItems = (PacketPlayOutWindowItems) packet;
                        int windowID = (int) ReflectionManager.getValue(packetPlayOutWindowItems, "a");

                        if (windowID == 1) {
                            net.minecraft.server.v1_8_R3.ItemStack[] nmsItems = (net.minecraft.server.v1_8_R3.ItemStack[]) ReflectionManager.getValue(packetPlayOutWindowItems, "b");
                            addPacket(player, new EntityChangeInventoryPacketContainer(nmsItems));
                        }
                    }

                    if (packet instanceof PacketPlayOutSetSlot) {
                        PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot) packet;
                        int windowID = (int) ReflectionManager.getValue(packetPlayOutSetSlot, "a");

                        if (windowID == 1) {
                            int slot = (Integer) ReflectionManager.getValue(packetPlayOutSetSlot, "b");
                            net.minecraft.server.v1_8_R3.ItemStack nmsItem = (net.minecraft.server.v1_8_R3.ItemStack) ReflectionManager.getValue(packetPlayOutSetSlot, "c");
                            addPacket(player, new EntityChangeInventoryItemPacketContainer(slot, nmsItem));
                        }
                    }

                    if (packet instanceof PacketPlayOutNamedSoundEffect) {
                        PacketPlayOutNamedSoundEffect playOutNamedSoundEffect = (PacketPlayOutNamedSoundEffect) packet;
                        String id = (String) ReflectionManager.getValue(playOutNamedSoundEffect, "a");
                        int x = (Integer) ReflectionManager.getValue(playOutNamedSoundEffect, "b");
                        int y = (Integer) ReflectionManager.getValue(playOutNamedSoundEffect, "c");
                        int z = (Integer) ReflectionManager.getValue(playOutNamedSoundEffect, "d");
                        float volume = (Float) ReflectionManager.getValue(playOutNamedSoundEffect, "e");

                        addPacket(player, new EntityPlayNamedSoundPacketContainer(id, volume, x, y, z));
                    }

                    if (packet instanceof PacketPlayOutEntityDestroy) {
                        PacketPlayOutEntityDestroy playOutEntityDestroy = (PacketPlayOutEntityDestroy) packet;
                        int[] a = (int[]) ReflectionManager.getValue(playOutEntityDestroy, "a");
                        System.out.println("IDs: " + a);
                    }
                }
            }
        });

        GameAPI.getInstance().registerEvents(new Listener() {
            //MCONE EVENTS
            @EventHandler
            public void on(PlayerRoundStatsChangeEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityStatsChangePacketContainer(e.getKills(), e.getDeaths(), e.getGoals()));
                    }
                }
            }

            @EventHandler
            public void on(TeamWonEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    session.getInfo().setWinnerTeam(e.getTeam().getName());
                }
            }

            @EventHandler
            public void on(PlayerJoinReplaySessionEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        ReplayPlayer replayPlayer = session.getReplayPlayer(player.getUniqueId());
                        replayPlayer.getData().setSpawnLocation(new CoreLocation(player.getLocation()));
                        addPacket(player, new EntitySpawnPacketContainer(player.getLocation()));
                    }
                }
            }

            @EventHandler
            public void on(PlayerQuitReplaySessionEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityDestroyPacketContainer(e.getPlayer().getUniqueId().toString()));
                    }
                }
            }

            @EventHandler
            public void on(ArmorEquipEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityArmorChangePacketContainer(e.getType(), e.getNewArmorPiece()));
                        if (isStopped()) {
                            e.getHandlers().unregister(this);
                        }
                    }
                }
            }

            //Bukkit events
            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerMoveEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        if (ticks % 2 == 0) {
                            addPacket(player, new EntityMovePacketContainer(player.getLocation()));
                        }
                    }
                }
            }

            @EventHandler
            public void on(BlockBreakEvent e) {
                Player player = e.getPlayer();

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityBlockBreakPacketContainer(e.getBlock()));
                    }
                }
            }

            @EventHandler
            public void on(BlockPlaceEvent e) {
                Player player = e.getPlayer();

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityBlockPlacePacketContainer(e.getBlock()));
                    }
                }
            }

            @EventHandler
            public void on(EntityDamageByEntityEvent e) {
                if (isStopped) {
                    e.getHandlers().unregister(this);
                } else {
                    org.bukkit.entity.Entity entity = e.getEntity();
                    org.bukkit.entity.Entity projectile = e.getDamager();

                    if (projectile != null && entity != null) {
                        if (entity instanceof Player) {
                            Player damaged = (Player) entity;
                            if ((projectile instanceof Arrow)) {
                                Arrow arrow = (Arrow) projectile;

                                if (arrow.getShooter() instanceof Player) {
                                    Player shooter = (Player) arrow.getShooter();
                                    if (session.existsReplayPlayer(shooter)) {
                                        addPacket(shooter, new EntityShootArrowPacketContainer(damaged.getLocation()));
                                        System.out.println("ARROW");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void on(EntityShootBowEvent e) {
                if (isStopped) {
                    e.getHandlers().unregister(this);
                } else {
                    Entity entity = e.getProjectile();

                    if (entity instanceof Arrow) {
                        if (e.getEntity() instanceof Player) {
                            Arrow arrow = (Arrow) entity;
                            Player player = (Player) e.getEntity();

                            if (session.existsReplayPlayer(player)) {
                                addPacket(player, new EntityShootArrowPacketContainer(arrow.getLocation()));
                                System.out.println("ARROW");
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void on(ProjectileLaunchEvent e) {
                if (isStopped) {
                    e.getHandlers().unregister(this);
                } else {
                    Projectile projectile = e.getEntity();
                    if (projectile instanceof Snowball) {
                        Snowball snowball = (Snowball) projectile;

                        if (snowball.getShooter() instanceof Player) {
                            Player shooter = (Player) snowball.getShooter();
                            if (session.existsReplayPlayer(shooter)) {
                                addPacket(shooter, new EntityLaunchProjectilePacketContainer(eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile.SNOWBALL));
                                System.out.println("SNOWBALL");
                            }
                        }
                    } else if (projectile instanceof Egg) {
                        Egg egg = (Egg) projectile;
                        if (egg.getShooter() instanceof Player) {
                            Player shooter = (Player) egg.getShooter();
                            if (session.existsReplayPlayer(shooter)) {
                                addPacket(shooter, new EntityLaunchProjectilePacketContainer(eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile.EGG));
                                System.out.println("EGG");
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void on(PlayerDeathEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getEntity();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityDeathEventPacketContainer());
                    }
                }
            }

            @EventHandler
            public void on(PlayerRespawnEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityRespawnPacketContainer(e.getRespawnLocation()));
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerInteractEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            if (e.getClickedBlock().getType().equals(org.bukkit.Material.TNT) && player.getItemInHand().getType().equals(org.bukkit.Material.FLINT_AND_STEEL)) {
                                //PRIME TNT
                                addPacket(player, new EntityPrimeTntPacketContainer(e.getClickedBlock().getLocation()));
                            } else if (e.getClickedBlock().getType().equals(org.bukkit.Material.STONE_BUTTON) || e.getClickedBlock().getType().equals(org.bukkit.Material.STONE_BUTTON)) {
                                org.bukkit.block.BlockState blockState = e.getClickedBlock().getState();
                                addPacket(player, new EntityButtonInteractPacketContainer(e.getClickedBlock().getLocation(), ((Button) blockState.getData()).isPowered()));
                                addPacket(player, new EntityClickPacketContainer());
                            } else if (e.getClickedBlock().getType().toString().contains("DOOR")) {
                                BlockState blockState = e.getClickedBlock().getState();
                                addPacket(player, new EntityOpenDoorPacketContainer(e.getClickedBlock().getLocation(), ((Door) blockState.getData()).isOpen()));
                                addPacket(player, new EntityClickPacketContainer());
                            }
                        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            addPacket(player, new EntityClickPacketContainer());
                        }
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(EntityDamageEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (e.getEntity() instanceof Player) {
                        Player player = (Player) e.getEntity();
                        if (session.existsReplayPlayer(player)) {
                            addPacket(player, new EntityDamagePacketContainer());
                        }
                    }
                }
            }

            @EventHandler
            public void on(PlayerPickupItemEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityPickItemUpPacketContainer(e.getItem().getEntityId(), e.getItem()));
                    }
                }
            }

            @EventHandler
            public void on(PlayerDropItemEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityDropItemPacketContainer(e.getItemDrop().getEntityId(), e.getItemDrop().getItemStack(), e.getItemDrop().getLocation()));
                    }
                }
            }

            //Potion effects
            @EventHandler
            public void on(PlayerItemConsumeEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (e.getItem().getType().equals(Material.POTION)) {
                        Potion potion = Potion.fromItemStack(e.getItem());
                        Player player = e.getPlayer();

                        if (potion != null) {
                            addPacket(player, new EntityPotionEffectPacketContainer(potion));
                        }
                    }
                }
            }

            //TODO: Check if that works
            @EventHandler
            public void on(PotionSplashEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    for (ReplayPlayer replayPlayer : session.getPlayers()) {
                        Player player = Bukkit.getPlayer(replayPlayer.getUuid());
                        if (e.getAffectedEntities().contains(player)) {
                            if (Potion.fromItemStack(e.getPotion().getItem()) != null) {
                                addPacket(player, new EntityPotionEffectPacketContainer(Potion.fromItemStack(e.getPotion().getItem())));
                            }
                        }
                    }
                }
            }

            //SERVER

            //Explosive
            @EventHandler
            public void on(EntityExplodeEvent e) {
                if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
                    addServerPacket(new EntityTntExplodePacketContainer(e.getLocation(), e.blockList()));
                }
            }

            @EventHandler
            public void on(BroadcastMessageEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    String sTick = String.valueOf(ticks);
                    if (session.getMessages().containsKey(sTick)) {
                        session.getMessages().get(sTick);
                    } else {
                        session.getMessages().put(sTick, new ArrayList<PacketContainer>() {{
                            add(new ServerBroadcastMessagePacketContainer(e.getBroadcast()));
                        }});
                    }
                }
            }
        });
    }

    private void addServerPacket(PacketContainer wrapper) {
        int chunkID = ticks / 600;
        lastTick = ticks;

        if (chunks.containsKey(chunkID)) {
            chunks.get(chunkID).addServerPacket(ticks, wrapper);
        } else {
            ReplayChunk chunk = new ReplayChunk();
            chunk.addServerPacket(ticks, wrapper);
            chunks.put(chunkID, chunk);
        }
    }

    private void addPacket(Player player, PacketContainer wrapper) {
        int chunkID = ticks / 600;
        lastTick = ticks;

        if (chunks.containsKey(chunkID)) {
            chunks.get(chunkID).addPacket(player.getUniqueId(), ticks, wrapper);
        } else {
            ReplayChunk chunk = new ReplayChunk();
            chunk.addPacket(player.getUniqueId(), ticks, wrapper);
            chunks.put(chunkID, chunk);
        }
    }

    public void stop() {
        session.getInfo().setLastTick(lastTick);
        session.getInfo().setStopped(System.currentTimeMillis() / 1000);
        isStopped = true;
        taskID.cancel();
    }
}
