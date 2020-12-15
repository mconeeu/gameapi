/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.listener;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.player.CorePlayerLoadedEvent;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.player.GamePlayerLoadedEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.player.GameAPIPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GamePlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(CorePlayerLoadedEvent e) {
        CorePlayer p = e.getPlayer();

        GameAPIPlayer gp = new GameAPIPlayer(p);
        GameAPIPlugin.getSystem().registerGamePlayer(gp);

        if (GamePlugin.isGamePluginInitialized()
                && GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)
                && GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)
        ) {
            GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory().addPlayer(p.bukkit());
        }

        Bukkit.getPluginManager().callEvent(new GamePlayerLoadedEvent(e, e.getBukkitPlayer(), p, gp));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (GamePlugin.isGamePluginInitialized()) {
            GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(e.getPlayer());

            if (gamePlayer != null) {
                gamePlayer.removeFromGame(true);
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)) {
                if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
                    GamePlugin.getGamePlugin().getGameHistoryManager().getCurrentGameHistory().getPlayer(player).setLeft(System.currentTimeMillis() / 1000);
                }
            }

            if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY_MANAGER)) {
                if (GamePlugin.getGamePlugin().hasOption(Option.USE_REPLAY_VIEW_MANAGER)) {
                    // TODO: implement replay recorder
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        GameAPIPlayer gp = GameAPIPlugin.getSystem().getGamePlayer(e.getPlayer());
        gp.saveData();

        GameAPIPlugin.getSystem().unregisterGamePlayer(gp);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (GameAPI.getInstance().getGamePlayer(e.getPlayer()).getState().equals(GamePlayerState.SPECTATING)) {
            CoreWorld gameWorld = CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getGameWorld());

            if (gameWorld != null) {
                Location respawnLocation = gameWorld.getLocation("game.spectator");

                if (respawnLocation != null) {
                    e.setRespawnLocation(respawnLocation);
                } else {
                    GameAPIPlugin.getSystem().sendConsoleMessage("§cCould not set Respawn Location for Spectator " + e.getPlayer().getName() + ". Location game.spectator is not set in GameWorld!");
                }
            } else {
                GameAPIPlugin.getSystem().sendConsoleMessage("§cCould not set Respawn Location for Spectator " + e.getPlayer().getName() + ". GameWorld from Config is null!");
            }
        }
    }

}