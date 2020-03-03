package eu.mcone.gameapi.api.map;

public interface MapRotationHandler {

    MapRotationHandler setRotationInterval(long roationInvterval) throws IllegalArgumentException;

    void startRotation() throws IllegalStateException;

    GameAPIMap getCurrentMap();

    long getLastRotation();

    long getRotationInterval();

}
