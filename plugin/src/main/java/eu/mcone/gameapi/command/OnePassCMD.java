package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnePassCMD extends CorePlayerCommand {
    public OnePassCMD() {
        super("OnePass");
    }

    //onepass add/remove <name> int

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(player);
            return true;
        }
        if (player.hasPermission("onepass.levelchange")) {
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[1]);
                GamePlayer gameTarget = GameAPI.getInstance().getGamePlayer(target);
                int amount = Integer.parseInt(args[2]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("add")) {
                        GameAPI.getInstance().getMessenger().send(player, "§2Du hast " + target.getName() + " §a" + amount + "§2 Level hinzugefügt!");
                        gameTarget.addOnePassLevel(amount);
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        GameAPI.getInstance().getMessenger().send(player, "§2Du hast " + target.getName() + "§a " + amount + "§2 Level entfernt!");
                        gameTarget.removeOnePassLevel(amount);
                    } else {
                        GameAPIPlugin.getInstance().getMessenger().send(player, "§4Bitte benutze: §c/onepass <add|remove> [<Spieler>] [Anzahl]");
                    }
                } else {
                    GameAPI.getInstance().getMessenger().send(player, "§4Der Spieler befindet sich nicht auf deinem Server");
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    GamePlayer gameTarget = GameAPI.getInstance().getGamePlayer(target);
                    GameAPIPlugin.getInstance().getMessenger().send(player, "§2Der Spieler hat §a" + gameTarget.getOneLevel() + "§2 Level und§a " + (gameTarget.getOneXp() + 1) + "§2 Xp");
                } else {
                    GameAPI.getInstance().getMessenger().send(player, "§4Der Spieler befindet sich nicht auf deinem Server");
                }
            } else {
                GameAPIPlugin.getInstance().getMessenger().send(player, "§4Bitte benutze: §c/onepass [<Spieler>] §4oder §c/onepass <add|remove> [<Spieler>] [Anzahl]");
            }
        } else {
            GameAPI.getInstance().getMessenger().send(player, "§4Du hast nicht genügend Rechte für diesen Befehl!");
        }

        return false;
    }
}