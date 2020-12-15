/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.map;

public interface MapRotationHandler {

    MapRotationHandler setRotationInterval(long roationInvterval) throws IllegalArgumentException;

    void startRotation() throws IllegalStateException;

    GameAPIMap getCurrentMap();

    long getLastRotation();

    long getRotationInterval();

    String getFormattedTimeUntilNextRotation();

}
