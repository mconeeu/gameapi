package eu.mcone.gameapi.api.replay.exception;

public class ReplaySessionNotFoundException extends Exception {

    public ReplaySessionNotFoundException() {
        super();
    }

    public ReplaySessionNotFoundException(String message) {
        super(message);
    }

    public ReplaySessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReplaySessionNotFoundException(Throwable cause) {
        super(cause);
    }
}
