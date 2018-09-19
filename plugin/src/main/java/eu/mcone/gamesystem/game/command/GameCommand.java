package eu.mcone.gamesystem.game.command;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.game.Team;
import org.bukkit.entity.Player;

public class GameCommand extends CorePlayerCommand {

    public GameCommand() {
        super("game");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                GameSystem.getInstance().getMessager().send(player, "§8§m---------- §r§8§lMCONE-GameSystem §8§m----------");
                GameSystem.getInstance().getMessager().send(player, "§7Entwickelt von §fTwinsterHD und §fRufi");
                GameSystem.getInstance().getMessager().send(player, "§r");
                GameSystem.getInstance().getMessager().send(player, "§7§oWir bemühen uns darum alle Systeme und Spielmodi so effizient wie möglich zu gestalten.");
                GameSystem.getInstance().getMessager().send(player, "§7§oDeshalb sind auch alle von uns verwendeten Plugins ausschließlich selbst entwickelt!");
                GameSystem.getInstance().getMessager().send(player, "§8§m---------- §r§8§lMCONE-GameSystem §8§m----------");
            }
        } else if (args.length == 4) {
            if (player.hasPermission("gamesystem.admin")) {
                if (args[0].equalsIgnoreCase("stage")) {
                        if (args[1].equalsIgnoreCase("set")) {
                            for (Team team : Team.values()) {
                                if (args[2].equalsIgnoreCase(team.getString())) {
                                    try {
                                        CoreSystem.getInstance().getWorldManager().getWorld(player.getWorld()).setLocation(team.getString() + ".stagePlace." + Integer.parseInt(args[3]), player.getLocation()).save();
                                        GameSystem.getInstance().getMessager().send(player, "§aDu hast für das Team " + team.getColor() + team + " §aden Stageplatz §7" + args[3] + " §agesetzt.");
                                    } catch (NumberFormatException e) {
                                        GameSystem.getInstance().getMessager().send(player, "§cDer angegebenen Stageplatz ist keine Zahl!");
                                    }
                                }
                            }
                        } else {
                            sendHelpTopic(player);
                        }
                } else {
                    sendHelpTopic(player);
                }
            } else {
                GameSystem.getInstance().getMessager().sendTransl(player, "");
            }
        } else {
            sendHelpTopic(player);
        }

        return false;
    }

    private void sendHelpTopic(Player player) {
        if (player.hasPermission("gamesystem.admin")) {
            GameSystem.getInstance().getMessager().send(player, "§c/game info");
            GameSystem.getInstance().getMessager().send(player, "§c/game stage set <team> <platz>");
        } else {
            GameSystem.getInstance().getMessager().send(player, "§cBitte benutze /game info");
        }
    }
}