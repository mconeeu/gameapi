/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.api.game.countdown.handler.IGameCountdown;
import eu.mcone.gamesystem.api.game.manager.map.IMapManager;
import eu.mcone.gamesystem.game.achivements.AchievementManager;
import eu.mcone.gamesystem.game.command.GameCommand;
import eu.mcone.gamesystem.game.countdown.LobbyCountdown;
import eu.mcone.gamesystem.game.countdown.RestartCountdown;
import eu.mcone.gamesystem.game.countdown.SpawnCountdown;
import eu.mcone.gamesystem.game.manager.map.MapManager;
import eu.mcone.gamesystem.game.manager.team.TeamManager;
import eu.mcone.gamesystem.game.player.GamePlayer;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.player.DamageLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSystem extends GameSystemAPI {

    @Getter
    private static GameSystem system;
    private Logger apiLog;

    @Getter
    private DamageLogger damageLogger;

    @Override
    public void onEnable() {
        setInstance(this);
        system = this;

        apiLog = GameSystemAPI.getInstance().getLogger();

        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        damageLogger = new DamageLogger();

        if (GameTemplate.getInstance() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendConsoleMessage("§aLoaded Player " + player.getName());
                //Creates a new GamePlayer object with the player from the collection
                new GamePlayer(player);
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_MAP_MANAGER)) {
                sendConsoleMessage("Create MapManager");
                //Create MapManager instance
                GameTemplate.getInstance().setMapManager(new MapManager(GameTemplate.getInstance(), IMapManager.Options.MAP_INVENTORY));
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_TEAM_MANAGER)) {
                sendConsoleMessage("Create TeamManager");
                //Create TeamManager instance
                GameTemplate.getInstance().setTeamManager(new TeamManager());
            }

            if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_ACHIEVEMENTS)) {
                //Create AchievementManager instance
                GameTemplate.getInstance().setAchievementManager(new AchievementManager());
                //Stores all achievements from the database.
                GameTemplate.getInstance().getAchievementManager().loadAchievements();
            }
        }

        sendConsoleMessage("§aRegistering Events...");
        registerEvents(
                new EntityDamageByEntity(),
                new PlayerAchievementAwarded(),
                new WeatherChange(),
                new AsyncPlayerChat(),
                new PlayerJoin(),
                new PlayerQuit(),
                new ServerListPing()
        );

        sendConsoleMessage("§aRegistering Commands...");
        registerCommands(
                new GameCommand()
        );

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

    @Override
    public IGameCountdown registerCountdown(GameCountdownID countdownID, int seconds) {
        if (GameTemplate.getInstance() != null && GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_GAME_STATE_HANDLER)) {
            try {
                if (countdownID.equals(GameCountdownID.LOBBY_COUNTDOWN)) {
                    return new LobbyCountdown(seconds);
                } else if (countdownID.equals(GameCountdownID.SPAWN_COUNTDOWN)) {
                    return new SpawnCountdown(seconds);
                } else if (countdownID.equals(GameCountdownID.RESTART_COUNTDOWN)) {
                    return new RestartCountdown(seconds);
                } else {
                    throw new GameSystemException("The specified GameCountdownID not exists");
                }
            } catch (GameSystemException e) {
                e.printStackTrace();
                GameSystemAPI.getInstance().getLogger().log(Level.SEVERE, "Exception in class GameSystem", e);
            }
            return null;
        } else {
            apiLog.log(Level.SEVERE, "No game countdown could be created because the option will not be created");
            return null;
        }
    }
}
