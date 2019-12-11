package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.listener.backpack.gadget.*;
import org.bukkit.entity.Player;

public class GadgetListener extends BackpackInventoryListener {

    static {
        plugin.registerEvents(
                new BombListener(plugin),
                new CoinBombListener(plugin),
                new EasterGunListener(plugin),
                //new EnderGunListener(plugin),
                new LoveGunListener(plugin),
                new SnowGunListener(plugin),
                new CobwebGub(plugin),
                new EnderPearlListener(plugin),
                new SplashPotionListener(plugin)
        );
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GameAPIPlayer<?> gamePlayer, Player p) {
        p.getInventory().setItem(3, item.getItem());
    }

}