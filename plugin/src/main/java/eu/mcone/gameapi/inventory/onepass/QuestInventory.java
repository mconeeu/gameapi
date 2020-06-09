package eu.mcone.gameapi.inventory.onepass;

import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.inventory.InventoryOption;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.onepass.Quests;
import eu.mcone.gameapi.api.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class QuestInventory extends CoreInventory {

    public QuestInventory(Player player) {
        super("§fOnePass Quests", player, InventorySlot.ROW_6, InventoryOption.FILL_EMPTY_SLOTS);
        GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(player);


        setItem(InventorySlot.ROW_2_SLOT_3, new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).displayName("§8//§oMCONE§8//").create());

        int i = 10;
        for (Quests quests : Quests.values()) {
            if (!gamePlayer.hasBackpackItem(quests.getBackpackItemCategory(), quests.getWinItem())) {
                setItem(i, quests.getItemStack(), e -> {
                    GameAPI.getInstance().getMessenger().send(player, quests.getQuestTask());
                });
                i++;
            }
        }

        setItem(InventorySlot.ROW_6_SLOT_1, new ItemBuilder(Material.IRON_DOOR, 1).displayName("§c§l↩ Zurück").create(), e -> GamePlugin.getGamePlugin().getOnePassManager().openOnePassInventory(player));

        openInventory();
    }
}
