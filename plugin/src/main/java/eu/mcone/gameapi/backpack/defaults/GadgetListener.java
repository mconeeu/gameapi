/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.backpack.defaults;

import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
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
                new DoubleJumpListener(plugin, handler),
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
                new SplashPotionListener(plugin, handler),
                new FireWorkListener(plugin, handler)
        );
    }

    public GadgetListener(Category category) {
        super(category);
    }

    @Override
    public void onBackpackInventoryClick(BackpackItem item, GamePlayer gamePlayer, Player p, boolean notify) {
        if (gamePlayer.getSettings().isEnableGadgets()) {
            p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), item.getItem());
            gamePlayer.setCurrentBackpackItem(item, DefaultCategory.GADGET);
        } else {
            p.closeInventory();
            plugin.getMessenger().sendInfo(p, "Du kannst keine Gadgets benutzen, da du sie deaktiviert hast. Aktiviere sie wieder in den ![Einstellungen]!");
        }
    }

    @Override
    public void onItemItemRemove(BackpackItem item, GamePlayer gp, Player p) {
        super.onItemItemRemove(item, gp, p);
        p.getInventory().setItem(plugin.getBackpackManager().getGadgetSlot(p), null);
    }
}
