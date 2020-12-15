/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.broadcast.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.game.GameDrawEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStopEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateTimeoutEndEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.scoreboard.InGameObjective;
import eu.mcone.gameapi.api.scoreboard.TeamTablist;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class InGameState extends GameState {

    @Getter
    @Setter
    private static Class<? extends CoreScoreboard> scoreboard;
    @Getter
    @Setter
    private static Class<? extends InGameObjective> objective;

    public InGameState() {
        this(0);
    }

    public InGameState(int timeout) {
        this(0, timeout);
    }

    public InGameState(int countdown, int timeout) {
        super("InGame", countdown, timeout);
    }

    @Override
    public void onTimeoutSecond(CorePlugin plugin, long second) {
        switch ((int) (getTimeout()-second)) {
            case 60*10:
            case 60*5:
            case 60*2:
            case 60: {
                GamePlugin.getGamePlugin().getMessenger().broadcast(new SimpleBroadcast("§7Das Spiel stoppt in §f"+((getTimeout()-second) / 60)+" Minuten§7!"));
            }
        }

        //Update IngameObjective scoreboard automatically
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            for (Player player : GamePlugin.getGamePlugin().getPlayerManager().getPlayers(GamePlayerState.PLAYING)) {
                CoreObjective objective = CoreSystem.getInstance().getCorePlayer(player).getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                if (objective instanceof InGameObjective) {
                    objective.reload();
                }
            }
        }
    }

    @Override
    public void onTimeoutEnd(GameStateTimeoutEndEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && !GamePlugin.getGamePlugin().getTeamManager().isDisableWinMethod()) {
            Bukkit.getPluginManager().callEvent(
                    new GameDrawEvent(GamePlugin.getGamePlugin().getTeamManager().calculateWinnerByKills())
            );
        }
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            try {
                if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)) {
                    if (scoreboard == null) {
                        scoreboard = TeamTablist.class;
                    }

                    for (GamePlayer gp : GamePlugin.getGamePlugin().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING)) {
                        gp.getCorePlayer().setScoreboard(scoreboard.newInstance());
                    }
                }

                if (objective == null) {
                    objective = InGameObjective.class;
                }

                for (GamePlayer gp : GamePlugin.getGamePlugin().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING)) {
                    gp.getCorePlayer().getScoreboard().setNewObjective(objective.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStop(GameStateStopEvent event) {
    }
}
