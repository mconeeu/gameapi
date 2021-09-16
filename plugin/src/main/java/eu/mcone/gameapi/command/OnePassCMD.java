/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CorePlayerCommand;
import eu.mcone.coresystem.api.bukkit.facades.Msg;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OnePassCMD extends CorePlayerCommand {

    public OnePassCMD() {
        super("OnePass");
    }

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
                        Msg.send(player, "§2Du hast " + target.getName() + " §a" + amount + "§2 Level hinzugefügt!");
                        gameTarget.addOnePassLevel(amount);
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        Msg.send(player, "§2Du hast " + target.getName() + "§a " + amount + "§2 Level entfernt!");
                        gameTarget.removeOnePassLevel(amount);
                    } else {
                        Msg.send(player, "§4Bitte benutze: §c/onepass <add|remove> [<Spieler>] [Anzahl]");
                    }
                } else {
                    Msg.send(player, "§4Der Spieler befindet sich nicht auf deinem Server");
                }
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    GamePlayer gameTarget = GameAPI.getInstance().getGamePlayer(target);
                    Msg.send(player, "§2Der Spieler hat §a" + gameTarget.getOneLevel() + "§2 Level und§a " + (gameTarget.getOneXp() + 1) + "§2 Xp");
                } else {
                    Msg.send(player, "§4Der Spieler befindet sich nicht auf deinem Server");
                }
            } else {
                Msg.send(player, "§4Bitte benutze: §c/onepass [<Spieler>] §4oder §c/onepass <add|remove> [<Spieler>] [Anzahl]");
            }
        } else {
            Msg.send(player, "§4Du hast nicht genügend Rechte für diesen Befehl!");
        }

        return false;
    }

    @Override
    public List<String> onPlayerTabComplete(Player p, String[] args) {
        if (args.length == 1) {
            String search = args[0];
            List<String> matches = new ArrayList<>();

            for (String arg : new String[]{"add", "remove"}) {
                if (arg.startsWith(search)) {
                    matches.add(arg);
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().startsWith(search)) {
                    matches.add(player.getName());
                }
            }

            return matches;
        } else if (args.length == 2) {
            String search = args[1];
            List<String> matches = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().startsWith(search)) {
                    matches.add(player.getName());
                }
            }

            return matches;
        }

        return Collections.emptyList();
    }

}
