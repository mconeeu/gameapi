package eu.mcone.gameapi.listener.backpack.gadget;

import eu.mcone.gameapi.api.GamePlugin;
import org.bukkit.event.Listener;

public abstract class GadgetListener implements Listener {

    protected GamePlugin<?> plugin;

    public GadgetListener(GamePlugin<?> plugin) {
        this.plugin = plugin;
    }

}
