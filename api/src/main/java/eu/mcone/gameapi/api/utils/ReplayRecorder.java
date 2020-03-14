package eu.mcone.gameapi.api.utils;

import eu.mcone.gameapi.api.replay.chunk.ReplayChunk;

import java.util.Map;

public interface ReplayRecorder {

    Map<Integer, ReplayChunk> getChunks();
}
