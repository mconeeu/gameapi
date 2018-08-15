/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.manager.team;

public enum Team {

    RED(0, "red", false, "§c", "§cRot", "red.bed", "red.spawn", "red.spawner.", "red.npc"),
    BLUE(0, "blue", false, "§3", "§3Blau", "blue.bed", "blue.spawn", "blue.spawner.", "blue.npc"),
    VIOLET(0, "violet", false, "§5", "§5Violet", "violet.bed", "violet.spawn", "violet.spawner.", "violet,npc"),
    YELLOW(0, "yellow", false, "§e", "§eGelb", "yellow.bed", "yellow.spawn", "yellow.spawner.", "yellow.npc"),
    GREEN(0, "green", false, "§2", "§2Grün", "green.bed", "green.spawn", "green.spawner.", "green.npc"),
    GRAY(0, "gray", false, "§7", "§7Grau", "gray.bed", "gray.spawn", "gray.spawner.", "gray.npc"),
    WHITE(0, "white", false, "§f", "§fWeiß", "white.bed", "white.spawn", "white.spawner.", "white.npc"),
    BLACK(0, "black", false, "§1", "§0Schwarz", "black.bed", "black.spawn", "black.spawner.", "black.npc"),
    ERROR(0, "error", false, "§4", "§4ERROR", "error", "error", "error", "error");

    private int value;
    private String team;
    private Boolean bed_alive;
    private String team_color;
    private String prefix;
    private String bedLocationKey;
    private String teamLocationKey;
    private String spawnerLocationKey;
    private String npcLocationKey;

    Team(int value,
         final String team,
         boolean bed_alive,
         final String team_color,
         final String prefix,
         final String bedLocationKey,
         final String teamLocationKey,
         final String spawnerLocationKey,
         final String npcLocationKey) {

        this.value = value;
        this.team = team;
        this.bed_alive = bed_alive;
        this.team_color = team_color;
        this.prefix = prefix;
        this.bedLocationKey = bedLocationKey;
        this.teamLocationKey = teamLocationKey;
        this.spawnerLocationKey = spawnerLocationKey;
        this.npcLocationKey = npcLocationKey;
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
        return this.bed_alive;
    }

    /**
     * returns a string if the respawn block of the team has already been destroyed or not
     * @return bed_alive type String
     */
    public String isBedAliveString() {
        if (this.bed_alive) {
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
        return this.prefix;
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
    public String getBedLocationKey() {
        return this.bedLocationKey;
    }

    /**
     * returns the Team spawn location key for the core-config
     * @return teamLocationKey type String
     */
    public String getTeamLocationKey() {
        return this.teamLocationKey;
    }

    /**
     * returns the Team item spawner location key
     * @return spawnerLocationKey type String
     */
    public String getSpawnerLocationKey() {
        return this.spawnerLocationKey;
    }

    /**
     * returns the Location key of the npc Shop
     * @return npcLocationKey type String
     */
    public String getNpcLocationKey() {
        return this.npcLocationKey;
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
     * @param bed_alive type boolean
     */
    public void setBedAlive(final boolean bed_alive) {
        this.bed_alive = bed_alive;
    }
}
