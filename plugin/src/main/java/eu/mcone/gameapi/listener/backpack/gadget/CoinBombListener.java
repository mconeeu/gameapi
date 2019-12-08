/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.core.util.Random;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class CoinBombListener extends GadgetListener {

    public CoinBombListener(GamePlugin<?> plugin) {
        super(plugin);
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().equals(DefaultItem.COINBOMB.getItemStack()) && (e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_AIR))) {
            Player p = e.getPlayer();
            GameAPIPlayer<?> gp = plugin.getGamePlayer(p);

            if (Bukkit.getOnlinePlayers().size() != 1) {
                p.sendMessage("§aDu hast die Coin Bombe erfolgreich gezündet!");
                DefaultItem.COINBOMB.remove(gp);
                p.getInventory().remove(p.getItemInHand());

                for (Player players : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                            players.sendMessage("§8[§7§l!§8] §fServer §8» §aEine Coin Bombe wurde von §e" + p.getName() + " §agezündet sie startet in §e3 Sekunden"), 20L);
                    players.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 7));

                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                            players.sendMessage("§8[§7§l!§8] §fServer §8» §aEine Coin Bombe wurde von §e" + p.getName() + " §agezündet sie startet in §e2 Sekunden"), 40L);

                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                            players.sendMessage("§8[§7§l!§8] §fServer §8» §aEine Coin Bombe wurde von §e" + p.getName() + " §agezündet sie startet in §41 Sekunden"), 60L);

                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {

                        Vector v = new Vector(players.getLocation().getX(), players.getLocation().getY(), players.getLocation().getZ());
                        v.normalize();
                        v.setY(0.9D);
                        v.multiply(1.5D);

                        players.setVelocity(v);
                        players.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                        players.playEffect(p.getLocation(), Effect.LAVA_POP, 1);
                        players.playEffect(p.getLocation(), Effect.LAVA_POP, 1);
                        players.playEffect(p.getLocation(), Effect.LAVA_POP, 1);
                        players.playEffect(p.getLocation(), Effect.LAVA_POP, 2);


                        int random = Random.randomInt(2300, 8000);
                        int coinbomstartbrandom = Random.randomInt(5500, 12000);

                        if (!players.getName().equalsIgnoreCase(p.getName())) {
                            players.sendMessage("§8[§7§l!§8] §fServer §8» §fDie Coin Bombe ist §lexplodiert§f du bekommst §e§l" + random + " Coins!");
                            CoreSystem.getInstance().getCorePlayer(players).addCoins(random);
                        } else {
                            p.sendMessage("§8[§7§l!§8] §fServer §8» §fDie Coin Bombe ist §lexplodiert§f du bekommst §e§l" + coinbomstartbrandom + " Coins!");
                            CoreSystem.getInstance().getCorePlayer(p).addCoins(coinbomstartbrandom);
                        }

                        DefaultItem.COINBOMB.remove(gp);
                    }, 80L);
                }
            } else {
                GameAPIPlugin.getSystem().getMessager().send(p, "§4Es müssen mindestens 2 Spieler Online sein!");
            }
        }
    }

}
