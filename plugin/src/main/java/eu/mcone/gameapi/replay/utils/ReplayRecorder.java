package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.BroadcastMessageEvent;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimpleRecorder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplaySessionEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplaySessionEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.record.packets.player.*;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerBroadcastMessagePacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.util.SerializableItemStack;
import eu.mcone.gameapi.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.replay.session.ReplaySession;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

        CoreSystem.getInstance().registerEvents(new Listener() {

            //---------------------------------- ENTITY ----------------------------------
            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerMoveEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        if (ticks % 2 == 0) {
                            addPacket(player, new EntityMovePacketWrapper(player.getLocation()));
                        }
                    }
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
                        addPacket(player, new EntitySpawnPacketWrapper(player.getLocation()));
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
                        addPacket(player, new EntityDestroyPacketWrapper(e.getPlayer().getUniqueId().toString()));
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerItemHeldEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntitySwitchItemPacketWrapper(player.getItemInHand()));
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
                            if (e.getClickedBlock().getType().equals(Material.STONE_BUTTON) || e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                                BlockState blockState = e.getClickedBlock().getState();
                                addPacket(player, new EntityButtonInteractPacketWrapper(e.getClickedBlock().getLocation(), ((Button) blockState.getData()).isPowered()));
                                addPacket(player, new EntityClickPacketWrapper());
                            } else if (e.getClickedBlock().getType().toString().contains("DOOR")) {
                                BlockState blockState = e.getClickedBlock().getState();
                                addPacket(player, new EntityOpenDoorPacketWrapper(e.getClickedBlock().getLocation(), ((Door) blockState.getData()).isOpen()));
                                addPacket(player, new EntityClickPacketWrapper());
                            }
                        } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            addPacket(player, new EntityClickPacketWrapper());
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
                            addPacket(player, new EntityDamagePacketWrapper());
                            addPacket(player, new EntityChangeHealthPacketWrapper(player.getHealth()));
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
//                        addPacket(player, new EntityDeathEventPacketWrapper());
                    }
                }
            }

            @EventHandler
            public void on(EntityDamageByEntityEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Entity entity = e.getEntity();
                    Entity projectile = e.getDamager();

                    if (projectile != null && entity != null) {
                        if (entity instanceof Player) {
                            Player damaged = (Player) entity;
                            if ((projectile instanceof Arrow)) {
                                Arrow arrow = (Arrow) projectile;

                                if (arrow.getShooter() instanceof Player) {
                                    Player shooter = (Player) arrow.getShooter();
                                    if (session.existsReplayPlayer(shooter)) {
                                        addPacket(shooter, new EntityShootArrowPacketWrapper(damaged.getLocation()));
                                    }
                                }
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
                                addPacket(shooter, new EntityLaunchProjectilePacketWrapper(EntityProjectile.SNOWBALL));
                            }
                        }
                    } else if (projectile instanceof Egg) {
                        Egg egg = (Egg) projectile;
                        if (egg.getShooter() instanceof Player) {
                            Player shooter = (Player) egg.getShooter();
                            if (session.existsReplayPlayer(shooter)) {
                                addPacket(shooter, new EntityLaunchProjectilePacketWrapper(EntityProjectile.EGG));
                            }
                        }
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
                        addPacket(player, new EntityRespawnPacketWrapper(e.getRespawnLocation()));
                    }
                }
            }

            @EventHandler
            public void on(EntityRegainHealthEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (e.getEntity() instanceof Player) {
                        Player player = (Player) e.getEntity();
                        if (session.existsReplayPlayer(player)) {
                            double health = ((Player) e.getEntity()).getHealth();
                            addPacket(player, new EntityChangeHealthPacketWrapper(health));
                        }
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerToggleSneakEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {

                        if (e.isSneaking()) {
                            addPacket(player, new EntitySneakPacketWrapper(EntityAction.START_SNEAKING));
                        } else {
                            addPacket(player, new EntitySneakPacketWrapper(EntityAction.STOP_SNEAKING));
                        }
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
                        addPacket(player, new EntityArmorChangePacketWrapper(e.getType(), e.getNewArmorPiece()));
                        if (isStopped()) {
                            e.getHandlers().unregister(this);
                        }
                    }
                }
            }

            @EventHandler
            public void on(InventoryClickEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = (Player) e.getWhoClicked();
                    Inventory inv = e.getClickedInventory();

                    if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && e.getCurrentItem() != null) {
                        if (session.existsReplayPlayer(player)) {
                            Map<Integer, SerializableItemStack> items = new HashMap<>();

                            int slot = 0;
                            for (ItemStack itemStack : player.getInventory().getContents()) {
                                if (itemStack != null && itemStack.getType() != null && !itemStack.getType().equals(Material.AIR)) {
                                    items.put(slot, new SerializableItemStack(itemStack));
                                }

                                slot++;
                            }

                            addPacket(player, new EntityChangeInventoryPacketWrapper(items));
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
                        addPacket(player, new EntityPickItemUpPacketWrapper(e.getItem()));
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
                        addPacket(player, new EntityDropItemPacketWrapper(e.getItemDrop().getItemStack()));
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
                            addPacket(player, new EntityPotionEffectPacketWrapper(potion));
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
                                addPacket(player, new EntityPotionEffectPacketWrapper(Potion.fromItemStack(e.getPotion().getItem())));
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void on(PlayerRoundStatsChangeEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    Player player = e.getPlayer();
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityStatsChangePacketWrapper(e.getKills(), e.getDeaths(), e.getGoals()));
                    }
                }
            }

            //---------------------------------- SERVER ----------------------------------
            @EventHandler
            public void on(BroadcastMessageEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    String sTick = String.valueOf(ticks);
                    if (session.getMessages().containsKey(sTick)) {
                        session.getMessages().get(sTick);
                    } else {
                        session.getMessages().put(sTick, new ArrayList<PacketWrapper>() {{
                            add(new ServerBroadcastMessagePacketWrapper(e.getBroadcast()));
                        }});
                    }
                }
            }

            @EventHandler
            public void on(TeamWonEvent e) {
                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    session.getInfo().setWinnerTeam(e.getTeam().getTeamEnum().getTeam());
                }
            }

            @EventHandler
            public void on(BlockBreakEvent e) {
                Player player = e.getPlayer();

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                } else {
                    if (session.existsReplayPlayer(player)) {
                        addPacket(player, new EntityBreakBlockPacketWrapper(e.getBlock()));
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
                        addPacket(player, new EntityPlaceBlockPacketWrapper(e.getBlock()));
                    }
                }
            }
        });
    }

    private void addPacket(Player player, PacketWrapper wrapper) {
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
