package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.backpack.handler.GamePetHandler;
import eu.mcone.gameapi.listener.backpack.PetTrackListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class PetListener extends BackpackInventoryListener {

    @Getter
    private static final GamePetHandler handler = new GamePetHandler();

    static {
        plugin.registerEvents(
                new PetTrackListener(handler)
        );
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GameAPIPlayer<?> gamePlayer, Player p) {
        p.playSound(p.getLocation(), Sound.ANVIL_USE, 7,7);
        p.closeInventory();
        p.sendMessage("§8[§7§l!§8] §fRucksack §8» §2Du hast dein §a"+item.getName()+"§2 erfolgreich zu dir gerufen!");

        Bukkit.getScheduler().runTask(plugin, () -> handler.spawnPet(p, item));
    }

    @Override
    public void setBackpackItems(CategoryInventory inv, Category category, Set<BackpackItem> categoryItems, GameAPIPlayer<?> gamePlayer, Player player) {
        super.setBackpackItems(inv, category, categoryItems, gamePlayer, player);
        //Todo: add animal despawn item
    }
}
