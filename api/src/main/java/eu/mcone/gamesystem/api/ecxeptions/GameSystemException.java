/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.ecxeptions;

public class GameSystemException extends Exception {

    public GameSystemException() {
        super();
    }

    public GameSystemException(String message) {
        super(message);
    }

    public GameSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameSystemException(Throwable cause) {
        super(cause);
    }

}
