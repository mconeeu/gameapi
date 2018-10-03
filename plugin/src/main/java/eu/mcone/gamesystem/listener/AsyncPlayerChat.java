package eu.mcone.gamesystem.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.api.gamestate.GameStateID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    @EventHandler
    public void on(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();

        if (GameTemplate.getInstance() != null) {
            GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

            if (GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.LOBBY)
                    || GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.END)) {

                e.setFormat(gamePlayer.getCorePlayer().getMainGroup().getPrefix() + CoreSystem.getInstance().getTranslationManager().get("system.bukkit.chat")
                        .replace("%Player%", p.getName())
                        .replace("Nachricht", e.getMessage()));
            } else if (GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.INGAME)) {
                if (gamePlayer.isPlaying()) {
                    if (message.startsWith("@all") || message.startsWith("@a") || message.startsWith("@")) {
                        e.setCancelled(true);

                        for (Player all : GameTemplate.getInstance().getPlaying()) {
                            e.setFormat(CoreSystem.getInstance().getTranslationManager().get("game.chat.global")
                                    .replace("%player%", gamePlayer.getTeam().getColor() + p.getName())
                                    .replace("%message%", e.getMessage().replace("@all", "")));

                            GameTemplate.getInstance().getMessager().sendTransl(all, "game.chat.global"
                                    .replace("%player%", gamePlayer.getTeam().getColor() + p.getName())
                                    .replace("%message%", e.getMessage().replace("@all", "")));
                        }
                    } else {
                        for (Player team : GameTemplate.getInstance().getChats().get(gamePlayer.getTeam())) {
                            e.setCancelled(true);
                            e.setFormat(CoreSystem.getInstance().getTranslationManager().get("game.chat.team")
                                    .replace("%team%", gamePlayer.getTeam().getPrefix())
                                    .replace("%player%", p.getName())
                                    .replace("%message%", e.getMessage()));

                            GameTemplate.getInstance().getMessager().sendTransl(team, "game.chat.team"
                                    .replace("%team%", gamePlayer.getTeam().getPrefix())
                                    .replace("%player%", p.getName())
                                    .replace("%message%", e.getMessage()));
                        }
                    }
                } else if (gamePlayer.isSpectator()) {
                    for (Player spectators : GameTemplate.getInstance().getSpectators()) {
                        e.setCancelled(true);
                        e.setFormat(CoreSystem.getInstance().getTranslationManager().get("game.chat.death")
                                .replace("%player%", spectators.getName())
                                .replace("%message%", e.getMessage()));

                        GameTemplate.getInstance().getMessager().sendTransl(spectators, "game.chat.death"
                                .replace("%player%", spectators.getName())
                                .replace("%message%", e.getMessage()));
                    }
                }
            } else if (GameSystem.getInstance().getGameStateHandler().hasGameState(GameStateID.ERROR)) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.sendMessage("§cEs ist ein Problem auf getreten, bitte melde diese einem Mcone Teammitglied. (Gamestate == Error)");
                }
            }
        }
    }
}
