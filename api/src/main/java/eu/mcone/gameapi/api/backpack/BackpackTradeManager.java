package eu.mcone.gameapi.api.backpack;

import org.bukkit.entity.Player;

public interface BackpackTradeManager {
    boolean hasTraidingRequest(Player player, Player target);

    Player getTraidingPartner(Player p);

    void cancelTraid(Player player);

    void makeTraidRequest(Player p, Player target) throws IllegalArgumentException;

    void openBackpackTraidInventory(Player player);
}
