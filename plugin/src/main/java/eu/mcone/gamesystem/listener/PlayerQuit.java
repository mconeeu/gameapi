/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Playing;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.api.game.gamestate.GameStateID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (GameTemplate.getInstance() != null) {
            GamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());
            if (gp != null) {
                gp.removeFromGame();

                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_TEAM_STAGE)
                        || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                    GameTemplate.getInstance().getTeamManager().getTeamStageHandler().removePlayerFromStage(gp);
                }

                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_GAME_STATE_HANDLER)
                        || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
                    if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)) {
                        IGameCountdown gameCountdown = GameTemplate.getInstance().getGameStateHandler().getGameCountdown(GameCountdownID.LOBBY_COUNTDOWN);

                        if (Playing.Min_Players.getValue() - GameTemplate.getInstance().getPlaying().size() > 0) {
                            gameCountdown.stopRunning();
                            gameCountdown.idle();
                        } else {
                            gameCountdown.stopIdling();
                            gameCountdown.run();
                        }

                        for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                            GameTemplate.getInstance().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.quit", CoreSystem.getInstance().getGlobalCorePlayer(cps.getUuid()))
                                    .replaceAll("%player%", p.getName())
                                    .replaceAll("%playing%", Integer.toString(GameTemplate.getInstance().getPlaying().size()))
                                    .replaceAll("%max%", Playing.Max_Players.getString()));

                            cps.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                        }
                    } else if (GameTemplate.getInstance().getGameStateHandler().hasGameState(GameStateID.INGAME)) {
                        for (CorePlayer cps : CoreSystem.getInstance().getOnlineCorePlayers()) {
                            GameTemplate.getInstance().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.quit.ingame", CoreSystem.getInstance().getGlobalCorePlayer(cps.getUuid())).replace("%player%", p.getName()));
                            cps.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                        }

                        gp.getStats().addLoses(1);
                        gp.getStats().addDeaths(1);
                        gp.destroy();

                        GameTemplate.getInstance().getTeamManager().checkChanceToWin();
                    }
                }
            }
        }
    }
}
