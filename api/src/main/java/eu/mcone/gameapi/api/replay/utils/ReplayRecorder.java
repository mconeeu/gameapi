package eu.mcone.gameapi.api.replay.utils;

import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;
import eu.mcone.gameapi.api.replay.packets.server.MessageWrapper;

import java.util.List;
import java.util.Map;

public interface ReplayRecorder {

    Map<Integer, ReplayChunk> getChunks();

    String getRecorderID();

    String getWorld();

    long getStarted();

    long getStopped();

    int getTicks();

    boolean isStop();

    void record();

    void stop();

    int getLastTick();

    void addMessage(MessageWrapper wrapper);
}
