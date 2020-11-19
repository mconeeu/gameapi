package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.handler.GamePetHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PetListener extends BackpackInventoryListener {

    @Getter
    private static final GamePetHandler handler = new GamePetHandler(plugin);

    public PetListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {
        gamePlayer.setCurrentBackpackItem(item, DefaultCategory.PET);

        Sound.save(p);
        p.closeInventory();

        if (notify) {
            plugin.getMessenger().send(p, "§2Du hast dein §a"+item.getName()+"§2 erfolgreich zu dir gerufen!");
        }

        Bukkit.getScheduler().runTask(plugin, () -> handler.spawnPet(p, item));
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);
        handler.despawnPet(p);
    }

}
