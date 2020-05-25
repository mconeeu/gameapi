package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.gadget.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class GadgetListener extends BackpackInventoryListener {

    static {
        plugin.registerEvents(
                new BombListener(plugin),
                new MakesListener(plugin),
                new CoinBombListener(plugin),
                new EasterGunListener(plugin),
                //new EnderGunListener(plugin),
                new LoveGunListener(plugin),
                new SnowGunListener(plugin),
                new CobwebGunListener(plugin),
                new BoatListener(plugin),
                new FurnaceListener(plugin),
                new FlyOneCarpetListener(plugin),
                new GrapplerListener(plugin),
                new EnderPearlListener(plugin),
                new SplashPotionListener(plugin)
        );
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p) {
        if (gamePlayer.getSettings().isEnableGadgets()) {
            if (p.hasPermission("lobby.silenthub")) {
                p.getInventory().setItem(plugin.getBackpackManager().getItemSlot(), item.getItem());
            } else {
                p.getInventory().setItem(plugin.getBackpackManager().getFallbackSlot(), item.getItem());
            }
        } else {
            p.closeInventory();
            GameAPI.getInstance().getMessenger().send(p, "Du kannst keine Gadgets benutzen, da du sie deaktiviert hast. Aktiviere sie wieder in den §fEinstellungen§7!");
        }
    }

}
