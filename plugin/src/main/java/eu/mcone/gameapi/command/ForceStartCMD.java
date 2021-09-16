/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import eu.mcone.gameapi.api.player.GamePlayerState;
import org.bukkit.command.CommandSender;

public class ForceStartCMD extends CoreCommand {

    public ForceStartCMD() {
        super("start", "system.game.start", "forcestart", "skip", "skipcountdown");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
                if (!GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                        || GamePlugin.getGamePlugin().getPlayerManager().getPlayers(GamePlayerState.PLAYING).size() >= GamePlugin.getGamePlugin().getPlayerManager().getMinPlayers()) {
                    if (GamePlugin.getGamePlugin().getGameStateManager().isCountdownRunning()) {
                        if (LobbyGameState.forceStart()) {
                            Msg.sendSuccess(sender, "Du hast den Lobby Countdown übersprungen!");
                        } else {
                            Msg.sendError(sender, "Du kannst den Countdown nicht mehr überspringen, das Spiel startet gleich!");
                        }
                    } else {
                        Msg.sendError(sender, "Zurzeit läuft kein Lobby Countdown!");
                    }
                } else {
                    Msg.sendError(sender, "Es sind nicht genügend Spieler online, um das Spiel zu starten!");
                }
            } else {
                Msg.sendError(sender, "Du kannst den Start Befehl nur während der Lobby-Phase benutzen!");
            }
        } else {
            Msg.sendError(sender, "In diesem Spielmodus kann keine Lobby-Phase übersprungen werden!");
        }

        return false;
    }

}
