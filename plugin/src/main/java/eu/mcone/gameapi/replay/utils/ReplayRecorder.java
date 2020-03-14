package eu.mcone.gameapi.replay.utils;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.BroadcastMessageEvent;
import eu.mcone.coresystem.api.bukkit.event.armor.ArmorEquipEvent;
import eu.mcone.coresystem.api.bukkit.npc.capture.SimpleRecorder;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    if (ticks % 3 == 0) {
                        addPacket(session.getReplayPlayer(player), ticks, new EntityMovePacketWrapper(player.getLocation()));
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(PlayerJoinReplaySessionEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    ReplayPlayer replayPlayer = session.getReplayPlayer(player.getUniqueId());
                    replayPlayer.getData().setWorld(player.getLocation().getWorld().getName());
                    addPacket(session.getReplayPlayer(player), ticks, new EntitySpawnPacketWrapper(player.getLocation()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(PlayerQuitReplaySessionEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntityDestroyPacketWrapper(e.getPlayer().getUniqueId().toString()));

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void on(PlayerItemHeldEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntitySwitchItemPacketWrapper(player.getItemInHand()));

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
                            addPacket(replayPlayer, ticks, new EntityButtonInteractPacketWrapper(e.getClickedBlock().getLocation(), ((Button) blockState.getData()).isPowered()));
                            addPacket(replayPlayer, ticks, new EntityClickPacketWrapper());
                        } else if (e.getClickedBlock().getType().toString().contains("DOOR")) {
                            BlockState blockState = e.getClickedBlock().getState();
                            addPacket(replayPlayer, ticks, new EntityOpenDoorPacketWrapper(e.getClickedBlock().getLocation(), ((Door) blockState.getData()).isOpen()));
                            addPacket(replayPlayer, ticks, new EntityClickPacketWrapper());
                        }
                    } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        addPacket(replayPlayer, ticks, new EntityClickPacketWrapper());
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
                        addPacket(replayPlayer, ticks, new EntityDamagePacketWrapper());
                        addPacket(replayPlayer, ticks, new EntityChangeHealthPacketWrapper(player.getHealth()));

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
                        addPacket(session.getReplayPlayer(player), ticks, new EntityChangeHealthPacketWrapper(health));
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
                        addPacket(replayPlayer, ticks, new EntitySneakPacketWrapper(EntityAction.START_SNEAKING));
                    } else {
                        addPacket(replayPlayer, ticks, new EntitySneakPacketWrapper(EntityAction.STOP_SNEAKING));
                    }

                    if (isStopped()) {
                        e.getHandlers().unregister(this);
                    }
                }
            }

            @EventHandler
            public void on(ArmorEquipEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    System.out.println(e.getType());
                    System.out.println(e.getNewArmorPiece());
                    System.out.println(e.getOldArmorPiece());
                    addPacket(session.getReplayPlayer(player), ticks, new EntityArmorChangePacketWrapper(e.getType(), e.getNewArmorPiece()));
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
                        System.out.println("ADD PACKET");

//                        replayPlayer.getPackets().addPacket(ticks, new EntityChangeInventoryPacketWrapper(e.getInventory().getContents()));
                    }
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(InventoryClickEvent e) {
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

                        addPacket(session.getReplayPlayer(player), ticks, new EntityChangeInventoryPacketWrapper(items));
                    }
                }
            }

            @EventHandler
            public void on(PlayerPickupItemEvent e) {
                e.getItem().remove();
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntityPickItemUpPacketWrapper(e.getItem()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(PlayerDropItemEvent e) {
                Player player = e.getPlayer();
                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntityDropItemPacketWrapper(e.getItemDrop().getItemStack()));
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
                            addPacket(replayPlayer, ticks, new EntityPotionEffectPacketWrapper(potion));
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
                            addPacket(replayPlayer, ticks, new EntityPotionEffectPacketWrapper(Potion.fromItemStack(e.getPotion().getItem())));
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
                    addPacket(session.getReplayPlayer(player), ticks, new EntityStatsChangePacketWrapper(e.getKills(), e.getDeaths(), e.getGoals()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            //---------------------------------- SERVER ----------------------------------
            @EventHandler
            public void on(BroadcastMessageEvent e) {
                String sTick = String.valueOf(ticks);
                if (session.getMessages().containsKey(sTick)) {
                    session.getMessages().get(sTick);
                } else {
                    session.getMessages().put(sTick, new ArrayList<PacketWrapper>() {{
                        add(new ServerBroadcastMessagePacketWrapper(e.getBroadcast()));
                    }});
                }

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

            @EventHandler
            public void on(BlockBreakEvent e) {
                Player player = e.getPlayer();

                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntityBreakBlockPacketWrapper(e.getBlock()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }

            @EventHandler
            public void on(BlockPlaceEvent e) {
                Player player = e.getPlayer();

                if (session.existsReplayPlayer(player)) {
                    addPacket(session.getReplayPlayer(player), ticks, new EntityPlaceBlockPacketWrapper(e.getBlock()));
                }

                if (isStopped()) {
                    e.getHandlers().unregister(this);
                }
            }
        });
    }

    private void addPacket(ReplayPlayer player, int tick, PacketWrapper wrapper) {
        int chunkID = tick / 600;

        if (chunks.containsKey(chunkID)) {
            System.out.println("ADD PACKET");
            chunks.get(chunkID).addPacket(player.getUuid(), tick, wrapper);
        } else {
            System.out.println("Create new chunk TICK:" + tick + " ID:" + chunkID);
            ReplayChunk chunk = new ReplayChunk(chunkID);
            chunk.addPacket(player.getUuid(), tick, wrapper);
            chunks.put(chunkID, chunk);
        }
    }

    public void stop() {
        session.getInfo().setLastTick(ticks);
        isStopped = true;
        taskID.cancel();
    }
}
