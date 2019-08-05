/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gamesystem.api.player.DamageLogger;
import lombok.Getter;
import org.bukkit.ChatColor;

public abstract class GameSystemAPI extends CorePlugin {

    public GameSystemAPI() {
        super("gamesystem", ChatColor.DARK_GRAY, "system.prefix.server");
    }

    @Getter
    private static GameSystemAPI instance;

    protected void setInstance(final GameSystemAPI instance) {
        if (instance == null) {
            System.err.println("GameSystem instance cannot be set twice!");
        } else {
            GameSystemAPI.instance = instance;
        }
    }

    /**
     * @return Returns the DamageLogger
     */
    public abstract DamageLogger getDamageLogger();
}
