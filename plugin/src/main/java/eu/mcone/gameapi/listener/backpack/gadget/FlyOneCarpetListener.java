/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.EulerAngle;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.HashSet;

public class FlyOneCarpetListener extends GadgetListener {


    public FlyOneCarpetListener(GamePlugin plugin, GameGadgetHandler handler) {
        super(plugin, handler);
    }

    private final HashSet<Player> isRiding = new HashSet<>();
    private final HashMap<Player, ArmorStand> armorStandMap = new HashMap<>();
    private final HashMap<Player, Integer> armorstandId = new HashMap<>();
    private final HashMap<Player, Integer> horseId = new HashMap<>();

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.FLY_CARPET.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();

            if (!p.isSprinting()) {
                if (p.isOnGround()) {
                    if (!isRiding.contains(p)) {
                        p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);


                        isRiding.add(p);
                        spawnHorse(p);
                        Sound.save(p);
                    } else {
                        Msg.send(p, "§4Bitte warte ein paar Sekunden...");
                    }
                } else {
                    Msg.send(p, "§4Du darfst dabei nicht in der Luft sein!");
                }
            } else {
                Msg.send(p, "§4Du darfst dabei nicht sprinten!");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {

        if (e.getEntity().getType().equals(EntityType.HORSE)) {
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                e.setCancelled(true);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                e.setCancelled(true);
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHorseDisamount(EntityDismountEvent e) {
        Player p = (Player) e.getEntity();

        if (e.getDismounted().getType().equals(EntityType.HORSE)) {
            if (isRiding.contains(p)) {

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    isRiding.remove(p);
                }, 50);
                e.getDismounted().remove();

                Sound.cancel(p);

                World world = p.getWorld();
                for (ArmorStand armorStand : world.getEntitiesByClass(ArmorStand.class)) {
                    if (armorStand != null) {
                        if (armorStandMap.containsValue(armorStand)) {
                            if (armorstandId.containsKey(p)) {
                                if (armorstandId.get(p).equals(armorStand.getEntityId())) {
                                    armorStandMap.remove(p);
                                    armorstandId.remove(p);
                                    horseId.remove(p);
                                    armorStand.remove();
                                }
                            }
                        }
                    }
                }

                p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), DefaultItem.FLY_CARPET.getItemStack());
            }
        }
    }

    @EventHandler
    public void onReload(PluginDisableEvent e) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            World world = all.getWorld();

            if (isRiding.contains(all)) {
                for (Horse horse : world.getEntitiesByClass(Horse.class)) {
                    if (horse != null) {
                        if (horseId.get(all).equals(horse.getEntityId())) {
                            if (horseId.containsKey(all)) {
                                horse.remove();
                            }
                        }
                    }
                }
            }

            for (ArmorStand armorStand : world.getEntitiesByClass(ArmorStand.class)) {
                if (world.getEntities().contains(armorStand)) {
                    if (armorStand != null) {
                        if (!armorstandId.isEmpty()) {
                            if (armorstandId.containsKey(all)) {
                                if (armorstandId.get(all).equals(armorStand.getEntityId())) {
                                    armorStandMap.remove(all);
                                    horseId.remove(all);
                                    armorStand.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUnload(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        World world = p.getWorld();

        if (isRiding.contains(p)) {
            for (Horse horse : world.getEntitiesByClass(Horse.class)) {
                if (horseId.containsValue(horse.getEntityId())) {
                    if (horseId.get(p).equals(horse.getEntityId())) {
                        if (horseId.containsKey(p)) {
                            horse.remove();
                        }
                    }
                }
            }
            for (ArmorStand armorStand : world.getEntitiesByClass(ArmorStand.class)) {
                if (armorStandMap.containsValue(armorStand)) {
                    if (armorstandId.containsKey(p)) {
                        if (armorstandId.get(p).equals(armorStand.getEntityId())) {
                            if (armorstandId.containsKey(p)) {
                                armorStandMap.remove(p);
                                horseId.remove(p);
                                armorstandId.remove(p);
                                armorStand.remove();
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (isRiding.contains(p)) {
            if (armorStandMap.containsKey(p)) {


                double rotation = (p.getLocation().getYaw() - 90) % 360;
                if (rotation < 0) {
                    rotation += 360.0;
                }
                if (0 <= rotation && rotation < 22.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0.9, -1.35, 0));
                    // W
                } else if (22.5 <= rotation && rotation < 67.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0.9, -1.35, 0.9));
                    // NW
                } else if (67.5 <= rotation && rotation < 112.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0, -1.35, 0.9));
                    //   N
                } else if (112.5 <= rotation && rotation < 157.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(-0.9, -1.35, 0.9));
                    // NE
                } else if (157.5 <= rotation && rotation < 202.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(-0.8, -1.35, 0));
                    //E
                } else if (202.5 <= rotation && rotation < 247.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(-0.9, -1.35, -0.9));
                    //SE
                } else if (247.5 <= rotation && rotation < 292.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0, -1.35, -0.9));
                    // S
                } else if (292.5 <= rotation && rotation < 337.5) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0.9, -1.35, -0.9));
                    //SW
                } else if (337.5 <= rotation && rotation < 360.0) {
                    armorStandMap.get(p).teleport(p.getLocation().add(0.7, -1.35, 0));
                    //W
                }
                p.playEffect(p.getLocation(), Effect.LAVA_POP, 1);
            }
        }
    }


    public void spawnHorse(Player p) {
        Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);

        ((CraftHorse) horse).getHandle().setVariant(-1);

        horse.setAdult();
        horse.setVariant(Horse.Variant.HORSE);
        horse.setTamed(true);
        ((CraftHorse) horse).setOwnerUUID(p.getUniqueId());
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horseId.put(p, horse.getEntityId());


        Bukkit.getScheduler().runTaskLater(GameAPI.getInstance(), () -> {
            horse.setPassenger(p);
            CoreSystem.getInstance().createActionBar()
                    .message("§f§oBenutze LSHIFT um abszusteigen")
                    .send(p);
        }, 2L);

        createArmorStand(p);

    }


    public void createArmorStand(Player p) {
        p.teleport(p.getLocation().add(1, 0, 0));
        ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(0, 0, 0), EntityType.ARMOR_STAND);

        armorStand.setHelmet(bannerDesign(p));
        armorStand.setNoDamageTicks(20 * 60 * 90);
        armorStand.setVisible(false);
        armorStand.setHeadPose(new EulerAngle(272f, 0f, 550F));
        armorStand.setGravity(false);
        armorstandId.put(p, armorStand.getEntityId());
        armorStandMap.put(p, armorStand);


    }

    public DyeColor getRank(Player player) {
        switch (CoreSystem.getInstance().getCorePlayer(player).getMainGroup()) {
            case ADMIN:
                return DyeColor.RED;
            case CONTENT:
                return DyeColor.BLUE;
            case SRDEVELOPER:
            case DEVELOPER:
            case JRDEVELOPER:
                return DyeColor.CYAN;
            case SRBUILDER:
            case BUILDER:
            case JRBUILDER:
                return DyeColor.YELLOW;
            case SRMODERATOR:
            case MODERATOR:
                return DyeColor.LIME;
            case SUPPORTER:
            case JRSUPPORTER:
                return DyeColor.GREEN;
            case CREATOR:
                return DyeColor.PURPLE;
            case ONE:
                return DyeColor.LIGHT_BLUE;
            case PREMIUMPLUS:
            case PREMIUM:
                return DyeColor.ORANGE;
            case SPIELVERDERBER:
            case SPIELER:
            default:
                return DyeColor.GRAY;
        }
    }

    public ItemStack bannerDesign(Player p) {
        ItemStack i = new ItemStack(Material.BANNER, 1);
        BannerMeta m = (BannerMeta) i.getItemMeta();

        m.setBaseColor(getRank(p));
        i.setItemMeta(m);

        return i;
    }

}
