package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import org.bukkit.command.CommandSender;

public class MapCMD extends CoreCommand {

    public MapCMD() {
        super("map", "system.game.map");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        //Todo: implement /map rotate (to rotate to the next map, skipping the rotateInterval), /map (for general info)
        return false;
    }

}
