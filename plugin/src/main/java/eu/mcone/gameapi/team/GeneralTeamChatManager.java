package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
public class GeneralTeamChatManager implements Listener {

    private final TeamManager teamManager;

    public GeneralTeamChatManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        CorePlayer cp = CoreSystem.getInstance().getCorePlayer(player);

        String message = e.getMessage();

        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
            if (GamePlugin.getGamePlugin().getPlayerManager().isSpectator(player)) {
                for (Player spectator : GamePlugin.getGamePlugin().getPlayerManager().getSpectating()) {
                    spectator.sendMessage("§8[§7Spectator§8] " + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", player.getName()) + message);
                }
            } else {
                GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(player.getUniqueId());

                if (teamManager.getPlayingChat() != null) {
                    teamManager.getPlayingChat().onPlayingChat(message, player, gamePlayer);
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
                            receiver.playSound(receiver.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
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
