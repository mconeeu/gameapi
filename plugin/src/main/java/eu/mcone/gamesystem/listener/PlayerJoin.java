/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.gamestate.GameStateID;
import eu.mcone.gamesystem.game.player.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class PlayerJoin implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        GameSystem.getSystem().getDamageLogger().getPlayers().put(e.getPlayer().getUniqueId(), new HashMap<>());

        if (GameTemplate.getInstance() != null) {
            GamePlayer gp = new GamePlayer(p);

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_GAME_STATE_HANDLER)) {
                if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)) {
                    IGameCountdown gameCountdown = GameTemplate.getInstance().getGameStateHandler().getGameCountdown(GameCountdownID.LOBBY_COUNTDOWN);

                    if (Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size() <= 0) {
                        gameCountdown.stopIdling();
                        gameCountdown.run();
                    } else {
                        gameCountdown.idle();
                        gameCountdown.stopRunning();
                    }

                    p.setGameMode(GameMode.SURVIVAL);

                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setLevel(0);

                    p.getInventory().clear();
                    p.getInventory().setHelmet(null);
                    p.getInventory().setChestplate(null);
                    p.getInventory().setLeggings(null);
                    p.getInventory().setBoots(null);

                    p.getActivePotionEffects().clear();

                    for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                        GameTemplate.getInstance().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.join", cps)
                                .replace("%player%", p.getName())
                                .replace("%playing%", Integer.toString(GameTemplate.getInstance().getPlaying().size()))
                                .replace("%max%", Playing.Max_Players.getString()));

                        CoreSystem.getInstance().createTitle()
                                .stay(5)
                                .fadeIn(2)
                                .fadeOut(2)
                                .title(CoreSystem.getInstance().getTranslationManager().get("game.prefix", cps))
                                .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps)
                                        .replace("%player%", p.getName())
                                        .replace("%playing%", Integer.toString(GameTemplate.getInstance().getPlaying().size()))
                                        .replace("%max%", Playing.Max_Players.getString())).send(cps.bukkit());
                    }
                } else if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.INGAME)) {
                    gp.setSpectator(true);
                    p.setGameMode(GameMode.SPECTATOR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                    GameTemplate.getInstance().getMessager().sendTransl(p, "game.spectators.join");

                    CoreSystem.getInstance().createTitle()
                            .fadeIn(1)
                            .stay(3)
                            .fadeOut(1)
                            .title(CoreSystem.getInstance().getTranslationManager().get("bedwars.prefix", gp.getCorePlayer()))
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.spectators.join", gp.getCorePlayer()))
                            .send(p);
                }
            }
        }
    }
}
