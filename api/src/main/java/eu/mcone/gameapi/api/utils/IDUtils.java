/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.utils;

import java.util.Random;
import java.util.UUID;

public class IDUtils {

    /**
     * Generates a unique ID for the current replay
     * @return Unique SessionID as String
     */
    public static String generateID() {
        StringBuilder uuid = new StringBuilder();
        String[] uuidArray = UUID.randomUUID().toString().split("-");

        Random random = new Random(0);

        for (int i = 0; i < uuidArray.length / 2; i++) {
            uuid.append(uuidArray[random.nextInt(uuidArray.length)]);
        }

        return uuid.toString();
    }

}
