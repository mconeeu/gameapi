package eu.mcone.gamesystem.lobby.backpack;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.api.lobby.backpack.BackpackInventory;
import eu.mcone.gamesystem.api.lobby.cards.ItemCard;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class IngameInventory extends BackpackInventory {

    public IngameInventory(Player p) {
        super(p);

        GamePlayer gamePlayer = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());

        if (gamePlayer.getItemCards().size() > 0) {
            for (ItemCard itemCard : gamePlayer.getItemCards()) {
                addItem(itemCard.getItemCardBuilder().createStack());
            }
        } else {
            setItem(InventorySlot.ROW_3_SLOT_5, new ItemBuilder(Material.ITEM_FRAME, 1).displayName("§cDu hast keine Itemkarte(n)").create());
        }
    }
}


