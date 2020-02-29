package eu.mcone.gameapi.api.replay.exception;

public class GameModuleNotActiveException extends Exception {

    public GameModuleNotActiveException() {
        super();
    }

    public GameModuleNotActiveException(String message) {
        super(message);
    }

    public GameModuleNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameModuleNotActiveException(Throwable cause) {
        super(cause);
    }

}