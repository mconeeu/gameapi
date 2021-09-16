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
import eu.mcone.gameapi.backpack.GameBackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TradeCMD extends CorePlayerCommand {

    public TradeCMD() {
        super("trading", null, "trade");
    }

    @Override
    public boolean onPlayerCommand(Player player, String[] args) {
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            GamePlayer gt = GameAPI.getInstance().getGamePlayer(target);
            GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);

            if (gp.getSettings().isEnableTraiding()) {
                if (gt.getSettings().isEnableTraiding()) {


                    if (target != null) {
                        if (player != target) {
                            if (!((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().hasTraidingRequest(target, player)) {
                                GamePlugin.getGamePlugin().getBackpackManager().getTradeManager().makeTraidRequest(player, target);
                            } else {
                                ((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().openBackpackTraidInventory(player);
                            }
                        } else {
                            Msg.send(player, "§4Du kannst nicht mit dir selber handeln!");
                        }
                    } else {
                        Msg.send(player, "§4Der Spieler ist nicht online");
                    }
                } else {
                    Msg.send(player, "§4Du darfst diesen Spieler keine Tausch Anfrage schicken!");
                }
            } else {
                Msg.send(player, "§4Du hast das Tauschen ausgeschaltet!");
            }
            return true;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("accept")) {
                Player target = Bukkit.getPlayer(args[1]);
                GamePlayer gt = GameAPI.getInstance().getGamePlayer(target);
                GamePlayer gp = GameAPI.getInstance().getGamePlayer(player);
                if (gp.getSettings().isEnableTraiding()) {
                    if (gt.getSettings().isEnableTraiding()) {

                        if (((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().hasTraidingRequest(target, player)) {
                            ((GameBackpackManager) GamePlugin.getGamePlugin().getBackpackManager()).getTradeManager().openBackpackTraidInventory(player);
                        } else {
                            Msg.send(player, "§4Der §cSpieler §4hat dich nicht angefragt!");
                        }
                    } else {
                        Msg.send(player, "§4Du darfst diesen Spieler keine Tausch Anfrage schicken!");
                    }
                } else {
                    Msg.send(player, "§4Du hast das Tauschen ausgeschaltet!");
                }

                return true;
            }
        }

        Msg.send(player, "§4Bitte benutze: §c/trade [<accept>] <Spieler>");
        return false;
    }

    @Override
    public List<String> onPlayerTabComplete(Player p, String[] args) {
        String search = args[args.length - 1];
        List<String> matches = new ArrayList<>();

        if (args.length == 1 && "accept".startsWith(search)) {
            matches.add("accept");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != p && player.getName().startsWith(search)) {
                matches.add(player.getName());
            }
        }

        return matches;
    }

}
