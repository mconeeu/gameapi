/*
 * Copyright (c) 2017 - 2019 Rufus Maiwald, Marvin Hülsmann, Dominik Lippl and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.backpack;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Level {

    USUAL("§7§lGewöhnlich", ChatColor.GRAY, 8),
    UNUSUAL("§3§lUngewöhnlich", ChatColor.DARK_AQUA, 3),
    EPIC("§5§lEpisch", ChatColor.LIGHT_PURPLE, 10),
    LEGENDARY("§6§lLegendär", ChatColor.GOLD, 11),
    MYSTICAL("§c§lMystisch", ChatColor.RED,14);

    @Getter
    private String displayname;
    @Getter
    private ChatColor chatColor;
    @Getter
    private int glasSubId;

    Level(String displayname, ChatColor chatcolor, int glasSubID) {
        this.displayname = displayname;
        this.chatColor = chatcolor;
        this.glasSubId = glasSubID;
    }

}