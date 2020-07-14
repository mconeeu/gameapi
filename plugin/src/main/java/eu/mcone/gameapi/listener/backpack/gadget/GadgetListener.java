package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.listener.backpack.handler.GameGadgetHandler;
import org.bukkit.event.Listener;

public abstract class GadgetListener implements Listener {

    protected GamePlugin plugin;
    protected GameGadgetHandler handler;

    public GadgetListener(GamePlugin plugin, GameGadgetHandler handler) {
        this.plugin = plugin;
        this.handler = handler;
    }

}
