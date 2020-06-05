package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.map.GameAPIMap;
import eu.mcone.gameapi.map.GameMapManager;
import eu.mcone.gameapi.map.GameMapRotationHandler;
import org.bukkit.command.CommandSender;

public class MapCMD extends CoreCommand {

    private final GameMapManager manager;

    public MapCMD(GameMapManager manager) {
        super("map", "system.game.map");
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            StringBuilder sb = new StringBuilder("§7Folgende Welten sind geladen: ");
            for (GameAPIMap map : manager.getMaps()) {
                sb.append("§3").append(map.getName()).append("§7, ");
            }
            sb.append("\n");

            GameAPI.getInstance().getMessenger().send(sender, sb.toString());

            GameAPI.getInstance().getMessenger().send(sender, "§7MapRotationHandler: "+(manager.isRotationHandlerLoaded() ? "§2aktiv" : "§4nicht aktiv"));
            if (manager.isRotationHandlerLoaded()){
                GameAPI.getInstance().getMessenger().send(sender, "§7Aktuelle Rotation Map: "+manager.getMapRotationHandler().getCurrentMap());
                GameAPI.getInstance().getMessenger().send(sender, "§7Nächste Rotation: "+manager.getMapRotationHandler().getFormattedTimeUntilNextRotation()+"\n");
            }

            GameAPI.getInstance().getMessenger().send(sender, "§7MapVotingHandler: "+(manager.isVotingHandlerLoaded() ? "§2aktiv" : "§4nicht aktiv"));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("rotate")) {
            if (manager.isRotationHandlerLoaded()) {
                ((GameMapRotationHandler) manager.getMapRotationHandler()).rotate();
                GameAPI.getInstance().getMessenger().broadcast("§fDie Map wird gewechselt!");
                GameAPI.getInstance().getMessenger().send(sender, "§2Du hast die Map erfolgreich rotiert!");
                return true;
            } else {
                GameAPI.getInstance().getMessenger().send(sender, "§4Der RotationHandler wurde nicht initialisiert!");
                return false;
            }
        }

        GameAPI.getInstance().getMessenger().send(sender, "§4Bitte benutze:§c /map [<rotate>]");
        return false;
    }

}
