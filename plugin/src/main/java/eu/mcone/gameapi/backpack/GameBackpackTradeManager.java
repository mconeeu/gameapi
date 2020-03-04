package eu.mcone.gameapi.backpack;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackTradeManager;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.inventory.backpack.trade.TradeChooseInventory;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBackpackTradeManager implements BackpackTradeManager {

    private final GameBackpackManager manager;

    @Getter
    private final Map<Player, Player> inTrade;
    @Getter
    private final Map<Player, BackpackItem> choosedItems;
    @Getter
    private final List<Player> tradeItemAccepted;

    public GameBackpackTradeManager(GameBackpackManager manager) {
        this.manager = manager;

        this.inTrade = new HashMap<>();
        this.choosedItems = new HashMap<>();
        this.tradeItemAccepted = new ArrayList<>();
    }

    @Override
    public boolean hasTraidingRequest(Player player, Player target) {
        return inTrade.containsKey(player) && inTrade.get(player) == target;
    }

    @Override
    public Player getTraidingPartner(Player p) {
        for (Map.Entry<Player, Player> entry : inTrade.entrySet()) {
            if (entry.getKey().equals(p)) {
                return entry.getValue();
            } else if (entry.getValue().equals(p)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void cancelTraid(Player player) {
        if (inTrade.containsKey(player) || inTrade.containsValue(player)) {
            Player partner = getTraidingPartner(player);

            if (partner != null) {
                GameAPIPlugin.getSystem().getMessager().send(partner, "§4Dein Traiding Partner hat das Handeln abgebrochen. Es wurden keine Items verkauft.");
                inTrade.remove(inTrade.containsKey(player) ? player : partner);
            } else {
                inTrade.remove(player);
            }

            GameAPIPlugin.getSystem().getMessager().send(player, "§4Du hast das Traiding abgebrochen, da du das Inventar geschlossen hast. Es wurden keine Items verkauft.");
        } else {
            throw new IllegalStateException("Player is not traiding!");
        }
    }

    @Override
    public void makeTraidRequest(Player p, Player target) throws IllegalArgumentException {
        if (!hasTraidingRequest(p, target)) {
            if (!inTrade.containsKey(p)) {
                if (!inTrade.containsValue(target) && !inTrade.containsKey(target)) {
                    target.spigot().sendMessage(new ComponentBuilder("§cDer Spieler §f" + p.getName())
                            .append("§c hat dich zum Trade eingeladen ")
                            .append("§f[Klicke hier!]")
                            .bold(true)
                            .event(new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("§7§oKlicke hier, um die Tausch Einladung anzunehemen!").create()
                            ))
                            .event(new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/trade accept " + p.getName()
                            ))
                            .append("\n§c»")
                            .create());

                    inTrade.put(p, target);
                    openBackpackTraidInventory(p);
                } else {
                    GameAPI.getInstance().getMessager().send(p, "§cDer Spieler ist bereits in einem Tausch!");
                }
            } else {
                GameAPI.getInstance().getMessager().send(p, "§cDu bist bereits in einem §4Tausch!");
            }
        } else {
            GameAPI.getInstance().getMessager().send(p, "§4Du hast §c"+target.getName()+"§4 schon eine Anfrage geschickt");
        }
    }

    @Override
    public void openBackpackTraidInventory(Player player) {
        new TradeChooseInventory(player, manager.getItemCategory(DefaultCategory.GADGET.name()).getCategory());
    }

}
