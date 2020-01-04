package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ItemCMD extends CoreCommand {

    private final GameBackpackManager manager;

    public ItemCMD(GameBackpackManager manager) {
        super("item", "system.game.item", "items");
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                Player t = Bukkit.getServer().getPlayer(args[1]);

                if (t == null) {
                    GameAPIPlugin.getInstance().getMessager().send(p, "§4Der Spieler " + args[1] + " ist offline!");
                    return false;
                } else {
                    GamePlayer gp = GameAPIPlugin.getSystem().getGamePlayer(t);
                    Set<BackpackItem> items = manager.getCategoryItems(args[2]);

                    if (items != null) {
                        try {
                            for (BackpackItem item : items) {
                                if (item.getId() == Integer.parseInt(args[3])) {
                                    if (!gp.hasBackpackItem(args[2], Integer.parseInt(args[3]))) {
                                        gp.addBackpackItem(args[2], item);

                                        if (p.equals(t)) {
                                            GameAPIPlugin.getInstance().getMessager().send(p, "§2Du hast dir das Item §a" + item.getName() + "§2 hinzugefügt");
                                        } else {
                                            GameAPIPlugin.getInstance().getMessager().send(p, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + t.getName() + " §2hinzugefügt");
                                            GameAPIPlugin.getInstance().getMessager().send(t, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + p.getName() + " §2bekommen!");
                                        }

                                        return true;
                                    } else {
                                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Der Spieler besitzt dieses Item bereits!");
                                        return false;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            GameAPIPlugin.getInstance().getMessager().send(p, "§c" + args[3] + "§4 ist keine Item ID!");
                            return false;
                        }

                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Das Item mit der ID §c" + args[2] + "§4 existiert nicht!");
                        return false;
                    } else {
                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Die Kategorie " + args[2] + " existiert nicht!");
                        return false;
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                Player t = Bukkit.getServer().getPlayer(args[1]);

                if (t == null) {
                    GameAPIPlugin.getInstance().getMessager().send(p, "§4Der Spieler " + args[1] + " ist Offline!");
                    return false;
                } else {
                    GamePlayer gp = GameAPIPlugin.getSystem().getGamePlayer(t);
                    Set<BackpackItem> items = manager.getCategoryItems(args[2]);

                    if (items != null) {
                        try {
                            for (BackpackItem item : items) {
                                if (item.getId() == Integer.parseInt(args[3])) {
                                    if (gp.hasBackpackItem(args[2], Integer.parseInt(args[3]))) {
                                        gp.removeBackpackItem(args[2], item);

                                        if (p.equals(t)) {
                                            GameAPIPlugin.getInstance().getMessager().send(p, "§2Du hast dir das Item §a" + item.getName() + "§2 entfernt");
                                        } else {
                                            GameAPIPlugin.getInstance().getMessager().send(p, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + t.getName() + " §2gelöscht");
                                        }

                                        return true;
                                    } else {
                                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Der Spieler besitzt dieses Item nicht!");
                                        return false;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            GameAPIPlugin.getInstance().getMessager().send(p, "§c" + args[3] + "§4 ist keine Item ID!");
                            return false;
                        }

                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Das Item mit der ID §c" + args[2] + "§4 existiert nicht!");
                        return false;
                    } else {
                        GameAPIPlugin.getInstance().getMessager().send(p, "§4Die Kategorie " + args[2] + " existiert nicht!");
                        return false;
                    }
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            GameAPIPlugin.getInstance().getMessager().send(p, "https://systems.gitlab.onegaming.group/gamesystem/eu/mcone/gameapi/api/backpack/defaults/DefaultItem.html");
            GameAPIPlugin.getInstance().getMessager().send(p, "https://systems.gitlab.onegaming.group/lobby/eu/mcone/lobby/api/enums/Item.html");
            return true;
        }

        GameAPIPlugin.getInstance().getMessager().send(p, "§4Bitte benutze: §c/item <add|remove|list> [<Spieler>] [<Kategorie>] [<Item-name>]");
        return false;
    }
}
