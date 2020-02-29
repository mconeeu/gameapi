package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.BroadcastMessageEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimpleRecorder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.stats.PlayerRoundStatsChangeEvent;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.replay.event.PlayEffectEvent;
import eu.mcone.gameapi.api.replay.event.PlaySoundEvent;
import eu.mcone.gameapi.api.replay.event.PlayerJoinReplaySessionEvent;
import eu.mcone.gameapi.api.replay.event.PlayerQuitReplaySessionEvent;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.record.packets.player.*;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerBroadcastMessagePacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.server.ServerChangeStatePacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.world.WorldPlayEffectPacketWrapper;
import eu.mcone.gameapi.api.replay.record.packets.world.WorldPlaySoundPacketWrapper;
import eu.mcone.gameapi.replay.session.ReplaySession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.Button;
import org.bukkit.material.Door;
import org.bukkit.potion.Potion;

import java.util.ArrayList;

public class ReplayRecorder extends SimpleRecorder {

    private final ReplaySession session;

    public ReplayRecorder(final ReplaySession replaySession) {
        session = replaySession;
    }

    @Override
    public void record() {
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(CoreSystem.getInstance(), () -> ticks++, 1L, 1L);

        CoreSystem.getInstance().registerEvents(new Listener() {
            //---------------------------------- ENTITY ----------------------------------
            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerMoveEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntityMovePacketWrapper(player.getLocation()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(PlayerJoinReplaySessionEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntitySpawnPacketWrapper(player.getLocation()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(PlayerQuitReplaySessionEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntityDestroyPacketWrapper(e.getPlayer().getUniqueId().toString()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerItemHeldEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntitySwitchItemPacketWrapper(player.getItemInHand()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerInteractEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);

                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        if (e.getClickedBlock().getType().equals(Material.STONE_BUTTON) || e.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                            BlockState blockState = e.getClickedBlock().getState();
                            replayPlayer.addPacket(ticks, new EntityButtonInteractPacketWrapper(e.getClickedBlock().getLocation(), ((Button) blockState.getData()).isPowered()));
                            replayPlayer.addPacket(ticks, new EntityClickPacketWrapper());
                        } else if (e.getClickedBlock().getType().toString().contains("DOOR")) {
                            BlockState blockState = e.getClickedBlock().getState();
                            replayPlayer.addPacket(ticks, new EntityOpenDoorPacketWrapper(e.getClickedBlock().getLocation(), ((Door) blockState.getData()).isOpen()));
                            replayPlayer.addPacket(ticks, new EntityClickPacketWrapper());
                        }
                    } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        replayPlayer.addPacket(ticks, new EntityClickPacketWrapper());
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(EntityDamageEvent e) {
                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();
                    if (session.existsReplayPlayer(player)) {
                        ReplayPlayer replayPlayer = session.getReplayPlayer(player);

                        replayPlayer.addPacket(ticks, new EntityDamagePacketWrapper());
                        replayPlayer.addPacket(ticks, new EntityChangeHealthPacketWrapper(player.getHealth()));

                        if (isStopped()) {
                            e.getHandlers().unregister(this);
                        }
                    }
                }
            }

            @EventHandler
            public void on(EntityRegainHealthEvent e) {
                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();
                    if (session.existsReplayPlayer(player)) {
                        double health = ((Player) e.getEntity()).getHealth();
                        ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                        replayPlayer.addPacket(ticks, new EntityChangeHealthPacketWrapper(health));
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerToggleSneakEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);

                    if (e.isSneaking()) {
                        replayPlayer.addPacket(ticks, new EntitySneakPacketWrapper(EntityAction.START_SNEAKING));
                    } else {
                        replayPlayer.addPacket(ticks, new EntitySneakPacketWrapper(EntityAction.STOP_SNEAKING));
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            //Inventory and Armor
            @EventHandler
            public void on(InventoryClickEvent e) {
                Inventory inv = e.getClickedInventory();
                Player player = (Player) e.getWhoClicked();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    if (inv != null && !e.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && e.getCurrentItem() != null) {
                        if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                            replayPlayer.addPacket(ticks, new EntityArmorChangePacketWrapper(player.getInventory().getArmorContents()));
                        }
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(InventoryCloseEvent e) {
                Inventory inv = e.getInventory();
                Player player = (Player) e.getPlayer();

                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    System.out.println(e.getInventory().getType());
                    if (inv != null && (e.getInventory().getType().equals(InventoryType.CRAFTING) || e.getInventory().getType().equals(InventoryType.PLAYER))) {
                        replayPlayer.addPacket(ticks, new EntityChangeInventoryPacketWrapper(e.getInventory().getContents()));
                    }
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlayerPickupItemEvent e) {
                e.getItem().remove();
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntityPickItemUpPacketWrapper(e.getItem()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlayerDropItemEvent e) {
                Player player = e.getPlayer();
                ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                if (replayPlayer != null) {
                    replayPlayer.addPacket(ticks, new EntityDropItemPacketWrapper(e.getItemDrop().getItemStack()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            //Potion effects
            @EventHandler
            public void on(PlayerItemConsumeEvent e) {
                if (e.getItem().getType().equals(Material.POTION)) {
                    Potion potion = Potion.fromItemStack(e.getItem());
                    Player player = e.getPlayer();
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);

                    if (replayPlayer != null) {
                        if (potion != null) {
                            replayPlayer.addPacket(ticks, new EntityPotionEffectPacketWrapper(potion));
                        }
                    }
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            //TODO: Check if that works
            @EventHandler
            public void on(PotionSplashEvent e) {
                for (ReplayPlayer replayPlayer : session.getPlayers()) {
                    if (e.getAffectedEntities().contains(Bukkit.getPlayer(replayPlayer.getUuid()))) {

                        if (Potion.fromItemStack(e.getPotion().getItem()) != null) {
                            replayPlayer.addPacket(ticks, new EntityPotionEffectPacketWrapper(Potion.fromItemStack(e.getPotion().getItem())));
                        }
                    }
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlayerRoundStatsChangeEvent e) {
                Player player = e.getPlayer();

                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player);
                    replayPlayer.addPacket(ticks, new EntityStatsChangePacketWrapper(e.getKills(), e.getDeaths(), e.getGoals()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            //---------------------------------- SERVER ----------------------------------
            @EventHandler
            public void on(BroadcastMessageEvent e) {
                addServerPacket(ticks, new ServerBroadcastMessagePacketWrapper(e.getBroadcast()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(GameStateStartEvent e) {
                addServerPacket(ticks, new ServerChangeStatePacketWrapper(e.getPrevious().getName(), e.getCurrent().getName()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(TeamWonEvent e) {
                session.getInfo().setWinnerTeam(e.getTeam().getTeamEnum().getTeam());

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            //---------------------------------- WORLD ----------------------------------
            @EventHandler
            public void on(BlockBreakEvent e) {
                addWorldPacket(ticks, new EntityBreakBlockPacketWrapper(e.getBlock()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(BlockPlaceEvent e) {
                addWorldPacket(ticks, new EntityPlaceBlockPacketWrapper(e.getBlock()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlaySoundEvent e) {
                addWorldPacket(ticks, new WorldPlaySoundPacketWrapper(e.getSound(), e.getLocation()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlayEffectEvent e) {
                addWorldPacket(ticks, new WorldPlayEffectPacketWrapper(e.getEffect(), e.getI(), e.getLocation()));

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }
        });
    }

    private void addWorldPacket(int tick, PacketWrapper wrapper) {
        String sTick = String.valueOf(tick);
        if (session.getWorldPackets().containsKey(sTick)) {
            session.getWorldPackets().get(sTick).add(wrapper);
        } else {
            session.getWorldPackets().put(sTick, new ArrayList<PacketWrapper>() {{
                add(wrapper);
            }});
        }
    }

    private void addServerPacket(int tick, PacketWrapper wrapper) {
        String sTick = String.valueOf(tick);
        if (session.getServerPackets().containsKey(sTick)) {
            session.getServerPackets().get(sTick).add(wrapper);
        } else {
            session.getServerPackets().put(sTick, new ArrayList<PacketWrapper>() {{
                add(wrapper);
            }});
        }
    }

    public void stop() {
        session.getInfo().setLength(ticks);
        isStopped = true;
        taskID.cancel();
    }
}
