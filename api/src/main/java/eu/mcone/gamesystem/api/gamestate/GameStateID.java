/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

public enum GameStateID {

    ERROR(0),
    LOBBY(1),
    INGAME(2),
    END(3);

    private int value;

    GameStateID(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the Enum
     * @return returns the value.
     */
    public int getValue() {
        return value;
    }

}
