package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Modules;
import eu.mcone.gameapi.api.event.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.gamestate.common.InGameState;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GamePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(CorePlayerLoadedEvent e) {
        CorePlayer player = e.getPlayer();

        Bukkit.getScheduler().runTask(GameAPIPlugin.getInstance(), () -> {
            GameAPIPlugin.getSystem().registerGamePlayer(new GameAPIPlayer(player));
            Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(GameAPIPlugin.getSystem().getGamePlayer(player), GamePlayerLoadedEvent.Reason.JOINED));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(GamePlayerLoadedEvent e) {
        GamePlayer gamePlayer = e.getPlayer();
        Player player = e.getPlayer().getCorePlayer().bukkit();
        PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

        if (e.getReason().equals(GamePlayerLoadedEvent.Reason.JOINED)
                || e.getReason().equals(GamePlayerLoadedEvent.Reason.RELOADED)) {

            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
                if (GamePlugin.getGamePlugin().getModules().contains(Modules.REPLAY)) {
                    GamePlugin.getGamePlugin().getReplaySession().addPlayer(player);
                    GamePlugin.getGamePlugin().getReplaySession().getReplayPlayer(player).getData().setJoined(System.currentTimeMillis() / 1000);
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
                    GamePlugin.getGamePlugin().getMessager().send(cps.bukkit(), CoreSystem.getInstance().getTranslationManager().get("game.join", cps)
                            .replace("%player%", player.getName())
                            .replace("%playing%", Integer.toString(playerManager.getPlaying().size()))
                            .replace("%max%", String.valueOf(playerManager.getMaxPlayers())));

                    CoreSystem.getInstance().createTitle()
                            .stay(5)
                            .fadeIn(2)
                            .fadeOut(2)
                            .title(CoreSystem.getInstance().getTranslationManager().get("game.prefix", cps))
                            .subTitle(CoreSystem.getInstance().getTranslationManager().get("game.join.title", cps)
                                    .replace("%player%", player.getName())
                                    .replace("%playing%", Integer.toString(playerManager.getPlaying().size()))
                                    .replace("%max%", String.valueOf(playerManager.getMaxPlayers()))).send(cps.bukkit());
                }
            } else if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
                playerManager.setSpectating(player, true);
                GamePlugin.getGamePlugin().getMessager().sendTransl(player, "game.spectators.join");

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