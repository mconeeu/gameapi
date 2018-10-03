/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.game;

public enum Team {

    RED(0, "red", "§cRot", "§c", false, "red.bed", "red.spawn", "red.spawner.", "red.npc"),
    BLUE(0, "blue", "§9Blau", "§9", false, "blue.bed", "blue.spawn", "blue.spawner.", "blue.npc"),
    AQUA(0, "aqua", "§bTürkis", "§b", false, "aqua.bed", "aqua.spawn", "aqua.spawner.", "aqua.npc"),
    GREEN(0, "green", "§aGrün", "§a", false, "green.bed", "green.spawn", "green.spawner.", "green.npc"),
    YELLOW(0, "yellow", "§eGelb", "§e", false, "yellow.bed", "yellow.spawn", "yellow.spawner.", "yellow.npc"),
    GOLD(0, "gold", "§6Gold", "§6", false, "gold.bed", "gold.spawn", "gold.spawner.", "gold.npc"),
    GRAY(0, "gray", "§7Grau", "§7", false, "gray.bed", "gray.spawn", "gray.spawner.", "gray.npc"),
    WHITE(0, "white", "§fWeiß", "§f", false, "white.bed", "white.spawn", "white.spawner.", "white.npc"),
    BLACK(0, "black", "§0Schwarz", "§0", false, "black.bed", "black.spawn", "black.spawner.", "black.npc"),
    LIGHT_PURPLE(0, "lightPurple", "§dRosa", "§d", false, "lightPurple.bed", "lightPurple.spawn", "lightPurple.spawner.", "lightPurple.npc"),
    DARK_BLUE(0, "darkBlue", "§1Dunkelblau", "§1", false, "darkBlue.bed", "darkBlue.spawn", "darkBlue.spawner.", "darkBlue.npc"),
    DARK_GREEN(0, "darkGreen", "§2Dunkelgrün", "§2", false, "darkGreen.bed", "darkGreen.spawn", "darkGreen.spawner.", "darkGreen.npc"),
    DARK_AQUA(0, "darkAqua", "§3Aquamarin", "§3", false, "darkAqua.bed", "darkAqua.spawn", "darkAqua.spawner.", "darkAqua.npc"),
    DARK_RED(0, "darkRed", "§4Dunkelrot", "§4", false, "darkRed.bed", "darkRed.spawn", "darkRed.spawner.", "darkRed.npc"),
    DARK_PURPLE(0, "darkRed", "§5Lila", "§5", false, "darkRed.bed", "darkRed.spawn", "darkRed.spawner.", "darkRed.npc"),
    DARK_GRAY(0, "darkGray", "§8Dunkelgrau", "§8", false, "darkGray.bed", "darkGray.spawn", "darkGray.spawner.", "darkGray.npc"),
    ERROR(0, "error", "§8» §4ERROR §8«", "§4", false, "error", "error", "error", "error");

    private int value;
    private String team;
    private boolean team_alive;
    private String team_prefix;
    private String team_color;
    private String locationKeyBed;
    private String locationKeyTeam;
    private String locationKeySpawner;
    private String locationKeyNPC;

    Team(int value,
         final String team,
         final String team_prefix,
         final String team_color,
         boolean team_alive,
         final String locationKeyBed,
         final String locationKeyTeam,
         final String locationKeySpawner,
         final String locationKeyNPC) {

        this.value = value;
        this.team = team;
        this.team_prefix = team_prefix;
        this.team_color = team_color;
        this.team_alive = team_alive;
        this.locationKeyBed = locationKeyBed;
        this.locationKeyTeam = locationKeyTeam;
        this.locationKeySpawner = locationKeySpawner;
        this.locationKeyNPC = locationKeyNPC;
    }

    /**
     * returns the value of the Enum
     * @return value type Integer.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * returns a Bed Alive boolean of the enum
     * @return bed_alive type boolean
     */
    public boolean isBedAlive() {
        return this.team_alive;
    }

    /**
     * returns a string if the respawn block of the team has already been destroyed or not
     * @return bed_alive type String
     */
    public String isTeamAlive() {
        if (this.team_alive) {
            return "§a❤";
        } else {
            return "§c❤";
        }
    }

    /**
     * returns the string of the Enum
     * @return team type string
     */
    public String getString() {
        return this.team;
    }

    /**
     * returns the Prefix of the Enum
     * @return prefix type String
     */
    public String getPrefix() {
        return this.team_prefix;
    }

    /**
     * returns the color of the Team
     * @return team_color type String
     */
    public String getColor() {
        return this.team_color;
    }

    /**
     * returns the Bed location key for the core-config
     * @return bedLocationKey type String
     */
    public String getLocationKeyBed() {
        return this.locationKeyBed;
    }

    /**
     * returns the Team spawn location key for the core-config
     * @return teamLocationKey type String
     */
    public String getLocationKeyTeam() {
        return this.locationKeyTeam;
    }

    /**
     * returns the Team item spawner location key
     * @return spawnerLocationKey type String
     */
    public String getLocationKeySpawner() {
        return this.locationKeySpawner;
    }

    /**
     * returns the Location key of the npc Shop
     * @return npcLocationKey type String
     */
    public String getLocationKeyNPC() {
        return this.locationKeyNPC;
    }

    /**
     * set the value of the Enum
     * @param value type int
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     * set the bed_alive boolean to the defined boolean
     * @param team_alive type boolean
     */
    public void setTeamAlive(final boolean team_alive) {
        this.team_alive = team_alive;
    }
}
