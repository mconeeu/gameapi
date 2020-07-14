package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.listener.backpack.gadget.*;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class GadgetListener extends BackpackInventoryListener {

    @Getter
    private static final GameGadgetHandler handler = new GameGadgetHandler();

    static {
        plugin.registerEvents(
                new BombListener(plugin, handler),
                new MakesListener(plugin, handler),
                new CoinBombListener(plugin, handler),
                new EasterGunListener(plugin, handler),
                //new EnderGunListener(plugin),
                new LoveGunListener(plugin, handler),
                new SnowGunListener(plugin, handler),
                new CobwebGunListener(plugin, handler),
                new BoatListener(plugin, handler),
                new FurnaceListener(plugin, handler),
                new FlyOneCarpetListener(plugin, handler),
                new GrapplerListener(plugin, handler),
                new EnderPearlListener(plugin, handler),
                new SplashPotionListener(plugin, handler)
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
