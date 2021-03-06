/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.team.GameTeamManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
public class TeamChatListener implements Listener {

    private final GameTeamManager teamManager;

    public TeamChatListener(GameTeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        CorePlayer cp = CoreSystem.getInstance().getCorePlayer(player);
        GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);

        String message = e.getMessage();

        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
            if (gp.getState().equals(GamePlayerState.SPECTATING)) {
                for (Player spectator : GamePlugin.getGamePlugin().getPlayerManager().getPlayers(GamePlayerState.SPECTATING)) {
                    spectator.sendMessage("§8[§7Spectator§8] " + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", player.getName()) + message);
                }
            } else {
                GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(player.getUniqueId());

                if (teamManager.getTeamChatListener() != null) {
                    teamManager.getTeamChatListener().onPlayingChat(message, player, gamePlayer);
                }
            }

            e.setCancelled(true);
        } else {
            if (!cp.isVanished()) {
                for (Player receiver : Bukkit.getOnlinePlayers()) {
                    if (receiver != player) {
                        String targetMessage;

                        if (e.getMessage().contains(receiver.getName())) {
                            if (e.getMessage().contains("@" + receiver.getName())) {
                                targetMessage = e.getMessage().replaceAll("@" + receiver.getName(), "§b@" + receiver.getName() + "§7");
                                message = message.replaceAll("@" + receiver.getName(), ChatColor.AQUA + "@" + receiver.getName() + ChatColor.GRAY);
                            } else {
                                targetMessage = e.getMessage().replaceAll(receiver.getName(), "§b@" + receiver.getName() + "§7");
                                message = message.replaceAll(receiver.getName(), "§b@" + receiver.getName() + "§7");
                            }

                            e.getRecipients().remove(receiver);
                            receiver.sendMessage((cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", player.getName()) + targetMessage);
                            Sound.error(receiver);
                        }
                    }
                }

                e.setFormat(
                        (cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", player.getName())
                                + "%2$s"
                );

                e.getRecipients().remove(player);
                player.sendMessage((cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", player.getName()) + message);
            } else {
                e.setCancelled(true);
                CoreSystem.getInstance().getMessenger().send(player, "§4Bitte benutze §c/vc <message>§4 um eine Chatnachricht zu schreiben während du im Vanish-Modus bist!");
            }
        }
    }
}
