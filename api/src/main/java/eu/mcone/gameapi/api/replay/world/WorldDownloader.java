package eu.mcone.gameapi.api.replay.world;

import java.util.Collection;

public interface WorldDownloader {

    void runDownloader();

    Collection<String> getDownloadedWorlds();

    void stop();
}
