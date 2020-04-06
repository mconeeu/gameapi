package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.gamestate.common.LobbyGameState;
import org.bukkit.command.CommandSender;

public class ForceStartCMD extends CoreCommand {

    public ForceStartCMD() {
        super("start", "system.game.start", "forcestart", "skip", "skipcountdown");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof LobbyGameState) {
            if (!GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                    || GamePlugin.getGamePlugin().getPlayerManager().getPlaying().size() >= GamePlugin.getGamePlugin().getPlayerManager().getMinPlayers()) {
                if (GamePlugin.getGamePlugin().getGameStateManager().isCountdownRunning()) {
                    if (LobbyGameState.forceStart()) {
                        GameAPIPlugin.getSystem().getMessager().send(sender, "§2Du hast den Lobby Countdown übersprungen!");
                    } else {
                        GameAPIPlugin.getSystem().getMessager().send(sender, "§4Du kannst den Countdown nicht mehr überspringen, das Spiel startet gleich!");
                    }
                } else {
                    GameAPIPlugin.getSystem().getMessager().send(sender, "§4Zurzeit läuft kein Lobby Countdown!");
                }
            } else {
                GameAPIPlugin.getSystem().getMessager().send(sender, "§4Es sind nicht genügend Spieler online, um das Spiel zu starten!");
            }
        } else {
            GameAPIPlugin.getSystem().getMessager().send(sender, "§4Du kannst den Start Befehl nur während der Lobby-Phase benutzen!");
        }

        return false;
    }

}