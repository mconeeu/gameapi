/*
 * Copyright (c) 2017 - 2018 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.gamestate;

public abstract class GameState implements eu.mcone.gamesystem.api.gamestate.GameState {

    public abstract void init();

    public abstract void end();

    public abstract void register();
}
