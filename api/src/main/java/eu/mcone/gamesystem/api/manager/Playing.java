/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.manager;

public enum Playing {

    Min_Players(0),
    Max_Players(0);

    private int value;

    Playing(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the Enum
     * @return returns the value.
     */
    public int getValue() {
        return value;
    }

    /**
     * set the value of the Enum
     * @param playing_enum type Playing Enum
     * @param value        type int
     * @return new value of the enum
     */
    public int setValue(Playing playing_enum, int value) {
        return playing_enum.value = value;
    }

    /**
     * Returns the value of the Enum as String
     * @return returns the value
     */
    public String getString() {
        return Integer.toString(this.value);
    }

}
