/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.chat.SimpleBroadcast;
import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.scoreboard.LobbyObjective;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyGameState extends GameState {

    private static final int FORCE_START_TIME = 10;

    @Setter
    @Getter
    private static Class<? extends LobbyObjective> objective;

    public LobbyGameState() {
        this(30);
    }

    public LobbyGameState(int countdown) {
        super("Lobby", countdown);
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)
                && second <= 5
                && !GamePlugin.getGamePlugin().getTeamManager().isTeamsFinallySet()
        ) {
            GamePlugin.getGamePlugin().getTeamManager().setTeamsForRemainigPlayersByPriority();
            plugin.getMessenger().broadcast(new SimpleBroadcast("§2Die Teams wurden gesetzt"));
        }

        switch (second) {
            case 60:
            case 30:
            case 15:
            case 10:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plugin.getMessenger().sendTransl(p, "game.gamestate.lobby.countdown.idling", second);
                    Sound.done(p);
                }
            default:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setLevel(second);
                }
        }
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            CoreWorld lobbyWorld = CoreSystem.getInstance().getWorldManager().getWorld(
                    GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()
            );
            if (lobbyWorld != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    lobbyWorld.teleportSilently(p, "spawn");
                }
            }

            try {
                if (objective == null) {
                    objective = LobbyObjective.class;
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
    public void onCountdownEnd(GameStateCountdownEndEvent event) {
        super.onCountdownEnd(event);

        if (GamePlugin.getGamePlugin().hasModule(Module.BACKPACK_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.BACKPACK_MANAGER_REGISTER_TRAIL_CATEGORY)) {
                GamePlugin.getGamePlugin().getBackpackManager().getTrailHandler().stop();
            }

            if (GamePlugin.getGamePlugin().hasOption(Option.BACKPACK_MANAGER_REGISTER_PET_CATEGORY)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    GamePlugin.getGamePlugin().getBackpackManager().getPetHandler().despawnPet(player);
                }
            }
        }
    }

    public static boolean forceStart() {
        if (GamePlugin.getGamePlugin().getGameStateManager().getCountdownCounter() > FORCE_START_TIME) {
            return GamePlugin.getGamePlugin().getGameStateManager().updateCountdownCounter(FORCE_START_TIME);
        } else {
            return false;
        }
    }

}
