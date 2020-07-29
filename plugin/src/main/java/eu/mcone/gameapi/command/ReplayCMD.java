package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.replay.inventory.ReplayRecordingInventory;
import org.bukkit.entity.Player;

public class ReplayCMD extends CorePlayerCommand {

    public ReplayCMD() {
        super("replay");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
                if (!GamePlugin.getGamePlugin().getReplayManager().getRecording().isEmpty()) {
                    new ReplayRecordingInventory(player, GamePlugin.getGamePlugin().getReplay());
                } else {
                    new ReplayRecordingInventory(player, null);
                }

                return true;
            } else {
                GamePlugin.getGamePlugin().getMessenger().send(player, "§cDas ReplaySystem ist für diesen Spielmodus nicht aktiv!");
            }
        } else {
            GamePlugin.getGamePlugin().getMessenger().send(player, "§cBitte benutze /replay");
        }

        return false;
    }
}
