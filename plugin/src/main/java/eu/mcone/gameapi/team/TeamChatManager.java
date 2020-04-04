package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Getter
public class TeamChatManager implements Listener {

    @Setter
    private boolean disable;

    public TeamChatManager() {
        GamePlugin.getGamePlugin().registerEvents(this);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        if (!disable) {
            Player p = e.getPlayer();
            CorePlayer cp = CoreSystem.getInstance().getCorePlayer(p);

            String playerMessage = e.getMessage();

            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(p.getUniqueId());

                if (playerMessage.startsWith("@all")) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        GamePlugin.getGamePlugin().getMessager().sendSimple(all, "§8[§b@all§8] " + gamePlayer.getTeam().getTeam().getChatColor().toString() + p.getName() + " §8»§7" + playerMessage.replace("@all", ""));
                    }
                } else {
                    for (Player team : gamePlayer.getTeam().getPlayers()) {
                        GamePlugin.getGamePlugin().getMessager().sendSimple(team, gamePlayer.getTeam().getTeam().getChatColor().toString() + p.getName() + " §8»§7" + playerMessage);
                    }
                }

                e.setCancelled(true);
            } else {
                if (!cp.isVanished()) {
                    for (Player receiver : Bukkit.getOnlinePlayers()) {
                        if (receiver != p) {
                            String targetMessage;

                            if (e.getMessage().contains(receiver.getName())) {
                                if (e.getMessage().contains("@" + receiver.getName())) {
                                    targetMessage = e.getMessage().replaceAll("@" + receiver.getName(), "§b@" + receiver.getName() + "§7");
                                    playerMessage = playerMessage.replaceAll("@" + receiver.getName(), ChatColor.AQUA + "@" + receiver.getName() + ChatColor.GRAY);
                                } else {
                                    targetMessage = e.getMessage().replaceAll(receiver.getName(), "§b@" + receiver.getName() + "§7");
                                    playerMessage = playerMessage.replaceAll(receiver.getName(), "§b@" + receiver.getName() + "§7");
                                }

                                e.getRecipients().remove(receiver);
                                receiver.sendMessage((cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", p.getName()) + targetMessage);
                                receiver.playSound(receiver.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                            }
                        }
                    }

                    e.setFormat(
                            (cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", p.getName())
                                    + "%2$s"
                    );

                    e.getRecipients().remove(p);
                    p.sendMessage((cp.isNicked() ? Group.SPIELER.getPrefix() : cp.getMainGroup().getPrefix()) + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat").replaceAll("%Player%", p.getName()) + playerMessage);
                } else {
                    e.setCancelled(true);
                    CoreSystem.getInstance().getMessager().send(p, "§4Bitte benutze §c/vc <message>§4 um eine Chatnachricht zu schreiben während du im Vanish-Modus bist!");
                }
            }
        }
    }
}
