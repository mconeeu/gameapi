package eu.mcone.gameapi.api.map;

public interface MapRotationHandler {

    void setRotationInterval(long roationInvterval) throws IllegalArgumentException;

    void rotate() throws IllegalStateException;

}
