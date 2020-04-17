package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeCMD extends CoreCommand {

    public TradeCMD() {
        super("trading", null, "trade");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

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
                            GameAPI.getInstance().getMessenger().send(player, "§4Du kannst nicht mit dir selber handeln!");
                        }
                    } else {
                        GameAPI.getInstance().getMessenger().send(player, "§4Der Spieler ist nicht online");
                    }
                } else {
                    GameAPI.getInstance().getMessenger().send(player, "§4Du darfst diesen Spieler keine Tausch Anfrage schicken!");
                }
            } else {
                GameAPI.getInstance().getMessenger().send(player, "§4Du hast das Tauschen ausgeschaltet!");
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
                            GameAPI.getInstance().getMessenger().send(player, "§4Der §cSpieler §4hat dich nicht angefragt!");
                        }
                    } else {
                        GameAPI.getInstance().getMessenger().send(player, "§4Du darfst diesen Spieler keine Tausch Anfrage schicken!");
                    }
                } else {
                    GameAPI.getInstance().getMessenger().send(player, "§4Du hast das Tauschen ausgeschaltet!");
                }

                return true;
            }
        }

        GameAPI.getInstance().getMessenger().send(player, "§4Bitte benutze: §c/trade [<Spieler>]");
        return false;
    }
}
