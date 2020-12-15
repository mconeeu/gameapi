/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.command;

import eu.mcone.coresystem.api.bukkit.command.CoreCommand;
import eu.mcone.gameapi.listener.map.MapInventory;
import eu.mcone.gameapi.map.GameMapManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCMD extends CoreCommand {

    private final GameMapManager manager;

    public MapCMD(GameMapManager manager) {
        super("map", "system.game.map");
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            new MapInventory((Player) sender, manager);
        }
        return false;
    }

}
