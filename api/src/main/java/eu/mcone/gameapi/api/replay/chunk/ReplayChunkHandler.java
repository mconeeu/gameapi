package eu.mcone.gameapi.api.replay.chunk;

public interface ReplayChunkHandler {

    ReplayChunk createNewChunk(int ID);

    ReplayChunk getChunk(int tick);
}
