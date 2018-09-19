/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdown;
import eu.mcone.gamesystem.api.game.countdown.handler.GameCountdownID;
import eu.mcone.gamesystem.game.command.GameCommand;
import eu.mcone.gamesystem.game.countdown.LobbyCountdown;
import eu.mcone.gamesystem.game.countdown.RestartCountdown;
import eu.mcone.gamesystem.game.countdown.SpawnCountdown;
import eu.mcone.gamesystem.game.manager.map.MapManager;
import eu.mcone.gamesystem.game.manager.team.TeamManager;
import eu.mcone.gamesystem.gamestate.GameStateHandler;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.player.DamageLogger;
import lombok.Getter;

public class GameSystem extends GameSystemAPI {

    @Getter
    private DamageLogger damageLogger;
    @Getter
    private GameStateHandler gameStateHandler;

    @Override
    public void onEnable() {
        setInstance(this);
        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        damageLogger = new DamageLogger();
        gameStateHandler = new GameStateHandler();

        sendConsoleMessage("§aRegistering Events...");
        new EntityDamageByEntity();
        new PlayerAchievementAwarded();
        new WeatherChange();
        new AsyncPlayerChat();
        new PlayerJoin();
        new PlayerQuit();

        sendConsoleMessage("§aRegistering Commands...");
        CoreSystem.getInstance().getPluginManager().registerCoreCommand(new GameCommand(), this);

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

    @Override
    public MapManager createMapManager(CorePlugin instance) {
        return new MapManager(instance);
    }

    @Override
    public TeamManager createTeamManager() {
        return new TeamManager();
    }

    @Override
    public GameCountdown registerCountdown(GameCountdownID countdownID, int seconds) {
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
        }
        return null;
    }
}
