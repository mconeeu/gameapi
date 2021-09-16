/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.BackpackItemCategory;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemCMD extends CoreCommand {

    private final GameBackpackManager manager;

    public ItemCMD(GameBackpackManager manager) {
        super("item", "system.game.item", "items");
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add")) {
                Player t = Bukkit.getServer().getPlayer(args[1]);

                if (t == null) {
                    Msg.send(sender, "§4Der Spieler " + args[1] + " ist offline!");
                    return false;
                } else {
                    GamePlayer gp = GameAPIPlugin.getSystem().getGamePlayer(t);
                    Set<BackpackItem> items = manager.getCategoryItems(args[2]);

                    if (args[3].equalsIgnoreCase("*")) {
                        for (BackpackItem item : items) {
                            gp.addBackpackItem(args[2], item);
                        }

                        if (sender instanceof Player && sender.equals(t)) {
                            Msg.send(sender, "§2Du hast dir alle Items der Category " + args[2] + "hinzugefügt");
                        } else {
                            Msg.send(sender, "§2Du hast alle Items der Category §a" + args[2] + " §2vom Spieler §a" + t.getName() + " §2hinzugefügt");
                            Msg.send(t, "§2Du hast alle Items der Category §a" + args[2] + " §2vom Spieler §a" + sender.getName() + " §2bekommen!");
                        }

                        return true;
                    }

                    if (items != null) {
                        try {
                            for (BackpackItem item : items) {
                                if (item.getId() == Integer.parseInt(args[3])) {
                                    if (!gp.hasBackpackItem(args[2], Integer.parseInt(args[3]))) {
                                        gp.addBackpackItem(args[2], item);

                                        if (sender instanceof Player && sender.equals(t)) {
                                            Msg.send(sender, "§2Du hast dir das Item §a" + item.getName() + "§2 hinzugefügt");
                                        } else {
                                            Msg.send(sender, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + t.getName() + " §2hinzugefügt");
                                            Msg.send(t, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + sender.getName() + " §2bekommen!");
                                        }

                                        return true;
                                    } else {
                                        Msg.send(sender, "§4Der Spieler besitzt dieses Item bereits!");
                                        return false;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            Msg.send(sender, "§c" + args[3] + "§4 ist keine Item ID!");
                            return false;
                        }

                        Msg.send(sender, "§4Das Item mit der ID §c" + args[2] + "§4 existiert nicht!");
                        return false;
                    } else {
                        Msg.send(sender, "§4Die Kategorie " + args[2] + " existiert nicht!");
                        return false;
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                Player t = Bukkit.getServer().getPlayer(args[1]);

                if (t == null) {
                    Msg.send(sender, "§4Der Spieler " + args[1] + " ist Offline!");
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

                                        if (sender instanceof Player && sender.equals(t)) {
                                            Msg.send(sender, "§2Du hast dir das Item §a" + item.getName() + "§2 entfernt");
                                        } else {
                                            Msg.send(sender, "§2Du hast das Item §a" + item.getName() + " §2vom Spieler §a" + t.getName() + " §2gelöscht");
                                        }

                                        return true;
                                    } else {
                                        Msg.send(sender, "§4Der Spieler besitzt dieses Item nicht!");
                                        return false;
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            Msg.send(sender, "§c" + args[3] + "§4 ist keine Item ID!");
                            return false;
                        }

                        Msg.send(sender, "§4Das Item mit der ID §c" + args[2] + "§4 existiert nicht!");
                        return false;
                    } else {
                        Msg.send(sender, "§4Die Kategorie " + args[2] + " existiert nicht!");
                        return false;
                    }
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            Msg.send(sender, "https://systems.gitlab.onegaming.group/gamesystem/eu/mcone/gameapi/api/backpack/defaults/DefaultItem.html");
            Msg.send(sender, "https://systems.gitlab.onegaming.group/lobby/eu/mcone/lobby/api/enums/Item.html");
            return true;
        }

        Msg.send(sender, "§4Bitte benutze: §c/item <add|remove|list> [<Spieler>] [<Kategorie>] [<Item-ID>]");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length-1];

        if (args.length == 1) {
            for (String arg : new String[]{"add", "remove", "list"}) {
                if (arg.startsWith(search)) {
                    matches.add(arg);
                }
            }
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player != sender && player.getName().startsWith(search)) {
                    matches.add(player.getName());
                }
            }
        } else if (args.length == 3) {
            for (Category category : manager.getCategories()) {
                if (category.getName().startsWith(search)) {
                    matches.add(category.getName());
                }
            }
        } else if (args.length == 4) {
            BackpackItemCategory category = manager.getItemCategory(args[2]);

            if ("*".startsWith(search)) {
                matches.add("*");
            }
            if (category != null) {
                for (BackpackItem item : category.getItems()) {
                    String id = String.valueOf(item.getId());

                    if (id.startsWith(search)) {
                        matches.add(id);
                    }
                }
            }
        }

        return matches;
    }
}
