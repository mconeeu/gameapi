/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.event.GamePlayerLoadedEvent;
import eu.mcone.gamesystem.api.game.gamestate.GameStateID;
import eu.mcone.gamesystem.game.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameSystem.getSystem().getDamageLogger().getPlayers().put(e.getPlayer().getUniqueId(), new HashMap<>());

        if (GameTemplate.getInstance() != null) {
            loadPlayer(player);
        }
    }

    public static void loadPlayer(Player p) {
        Bukkit.getScheduler().runTask(GameSystemAPI.getInstance(), () -> {
            GamePlayer gp = new GamePlayer(p);
            Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(gp, GamePlayerLoadedEvent.Reason.JOINED));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(GamePlayerLoadedEvent e) {
        eu.mcone.gamesystem.api.game.player.GamePlayer gamePlayer = e.getPlayer();
        Player player = e.getPlayer().getCorePlayer().bukkit();

        if (e.getReason().equals(GamePlayerLoadedEvent.Reason.JOINED)
                || e.getReason().equals(GamePlayerLoadedEvent.Reason.RELOADED)) {

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ITEM_CARDS)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                gamePlayer.givePlayerRedeemedItemCards(GameTemplate.getInstance().getGamemode());
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_GAME_STATE_HANDLER)
                    || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)) {
                    IGameCountdown gameCountdown = GameTemplate.getInstance().getGameStateHandler().getGameCountdown(GameCountdownID.LOBBY_COUNTDOWN);

                    if (Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size() <= 0) {
                        gameCountdown.stopIdling();
                        gameCountdown.run();
                    } else {
                        gameCountdown.idle();
                        gameCountdown.stopRunning();
                    }

                    player.setGameMode(GameMode.SURVIVAL);

                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setLevel(0);

                    player.getInventory().clear();
                    player.getInventory().setHelmet(null);
                    player.getInventory().setChestplate(null);
                    player.getInventory().setLeggings(null);
                    player.getInventory().setBoots(null);

                    player.getActivePotionEffects().clear();

                    for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                        GameTemplate.getInstance().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.join", cps)
                                .replace("%player%", player.getName())
                                .replace("%playing%", Integer.toString(GameTemplate.getInstance().getPlaying().size()))
                                .replace("%max%", Playing.Max_Players.getString()));

                        CoreSystem.getInstance().createTitle()
                                .stay(5)
                                .fadeIn(2)
                                .fadeOut(2)
                                .title(CoreSystem.getInstance().getTranslationManager().get("game.prefix", cps))
                                .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps)
                                        .replace("%player%", player.getName())
                                        .replace("%playing%", Integer.toString(GameTemplate.getInstance().getPlaying().size()))
                                        .replace("%max%", Playing.Max_Players.getString())).send(cps.bukkit());
                    }
                } else if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.INGAME)) {
                    gamePlayer.setSpectator(true);
                    player.setGameMode(GameMode.SPECTATOR);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                    GameTemplate.getInstance().getMessager().sendTransl(player, "game.spectators.join");

                    CoreSystem.getInstance().createTitle()
                            .fadeIn(1)
                            .stay(3)
                            .fadeOut(1)
                            .title(CoreSystem.getInstance().getTranslationManager().get("bedwars.prefix", gamePlayer.getCorePlayer()))
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.spectators.join", gamePlayer.getCorePlayer()))
                            .send(player);
                }
            }
        }
    }
}
