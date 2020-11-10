package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.handler.GamePetHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PetListener extends BackpackInventoryListener {

    @Getter
    private static final GamePetHandler handler = new GamePetHandler(plugin);

    public PetListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.PET);

        p.playSound(p.getLocation(), Sound.ANVIL_USE, 7,7);
        p.closeInventory();
        p.sendMessage("§8[§7§l!§8] §fRucksack §8» §2Du hast dein §a"+item.getName()+"§2 erfolgreich zu dir gerufen!");

        Bukkit.getScheduler().runTask(plugin, () -> handler.spawnPet(p, item));
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);
        handler.despawnPet(p);
    }

}
