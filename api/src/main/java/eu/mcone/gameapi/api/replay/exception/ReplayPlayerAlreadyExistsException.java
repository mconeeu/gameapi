package eu.mcone.gameapi.api.replay.exception;

public class ReplayPlayerAlreadyExistsException extends Exception {

    public ReplayPlayerAlreadyExistsException() {
        super();
    }

    public ReplayPlayerAlreadyExistsException(String message) {
        super(message);
    }

    public ReplayPlayerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReplayPlayerAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
