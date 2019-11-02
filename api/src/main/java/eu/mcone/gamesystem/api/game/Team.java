/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum Team {

    RED(0, "red", "§cRot", ChatColor.RED, Color.RED, false, "game:red:bed", "game:red:spawn", "game:red:npc"),
    BLUE(0, "blue", "§9Blau", ChatColor.BLUE, Color.BLUE, false, "game:blue:bed", "game:blue:spawn", "game:blue:npc"),
    YELLOW(0, "yellow", "§eGelb", ChatColor.YELLOW, Color.YELLOW, false, "game:yellow:bed", "game:yellow:spawn", "game:yellow:npc"),
    GREEN(0, "green", "§aGrün", ChatColor.GREEN, Color.GREEN, false, "game:green:bed", "game:green:spawn", "game:green:npc"),
    ORANGE(0, "orange", "§6Orange", ChatColor.GOLD, Color.ORANGE, false, "game:gold:bed", "game:gold:spawn", "game:gold:npc"),
    AQUA(0, "aqua", "§5Türkisch", ChatColor.AQUA, Color.AQUA, false, "game:aqua:bed", "game:aqua:spawn", "game:aqua:npc"),
    WHITE(0, "white", "§fWeiß", ChatColor.WHITE, Color.WHITE, false, "game:white:bed", "game:white:spawn", "game:white:npc"),
    PURPLE(0, "purple", "§5Lila", ChatColor.LIGHT_PURPLE, Color.PURPLE, false, "game:purple:bed", "game:purple:spawn", "game:purple:npc"),
    ERROR(0, "error", "§8» §4ERROR §8«", ChatColor.RED, Color.RED, false, "error", "error", "error");

    private int value;
    private final String team;
    private final String prefix;
    private final ChatColor chatColor;
    private final Color color;
    private boolean team_alive;
    private final String locationKeyBed;
    private final String locationKeyTeam;
    private final String locationKeyNPC;

    Team(int value,
         final String team,
         final String prefix,
         final ChatColor chatColor,
         final Color color,
         final boolean team_alive,
         final String locationKeyBed,
         final String locationKeyTeam,
         final String locationKeyNPC) {

        this.value = value;
        this.team = team;
        this.prefix = prefix;
        this.chatColor = chatColor;
        this.color = color;
        this.team_alive = team_alive;
        this.locationKeyBed = locationKeyBed;
        this.locationKeyTeam = locationKeyTeam;
        this.locationKeyNPC = locationKeyNPC;
    }

    /**
     * returns the value of the Enum
     *
     * @return value type Integer.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * returns a Bed Alive boolean of the enum
     *
     * @return bed_alive type boolean
     */
    public boolean isBedAlive() {
        return this.team_alive;
    }

    /**
     * returns the string of the Enum
     *
     * @return team type string
     */
    public String getString() {
        return this.team;
    }

    /**
     * returns the Prefix of the Enum
     *
     * @return prefix type String
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * returns the chatcolor of the team
     *
     * @return chatcolor
     */
    public ChatColor getChatColor() {
        return this.chatColor;
    }

    /**
     * returns the color of the team
     *
     * @return color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * returns the Bed location key
     *
     * @return bedLocationKey type String
     */
    public String getLocationKeyBed() {
        return this.locationKeyBed;
    }

    /**
     * returns the Team spawn location key
     *
     * @return teamLocationKey type String
     */
    public String getLocationKeyTeam() {
        return this.locationKeyTeam;
    }

    /**
     * returns the Location key of the npc Shop
     *
     * @return npcLocationKey type String
     */
    public String getLocationKeyNPC() {
        return this.locationKeyNPC;
    }

    /**
     * set the value of the Enum
     *
     * @param value type int
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     * set the bed_alive boolean to the defined boolean
     *
     * @param team_alive type boolean
     */
    public void setBedAlive(final boolean team_alive) {
        this.team_alive = team_alive;
    }
}
