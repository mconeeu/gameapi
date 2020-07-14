package eu.mcone.gameapi.api.replay.chunk;

public interface ReplayChunkHandler {

    ReplayChunk createNewChunk();

    ReplayChunk getChunk(int tick);
}
