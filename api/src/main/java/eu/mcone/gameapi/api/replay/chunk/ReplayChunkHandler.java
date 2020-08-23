package eu.mcone.gameapi.api.replay.chunk;

public interface ReplayChunkHandler {

    int getChunkLength();

    ReplayChunk createNewChunk(int ID);

    ReplayChunk getChunk(int tick);

    ReplayChunk getChunkByID(int chunkID);
}
