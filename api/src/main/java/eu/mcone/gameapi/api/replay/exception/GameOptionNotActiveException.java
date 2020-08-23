package eu.mcone.gameapi.api.replay.exception;

public class GameOptionNotActiveException extends Exception {

    public GameOptionNotActiveException() {
        super();
    }

    public GameOptionNotActiveException(String message) {
        super(message);
    }

    public GameOptionNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameOptionNotActiveException(Throwable cause) {
        super(cause);
    }

}
