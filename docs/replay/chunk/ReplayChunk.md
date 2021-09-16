The ReplayChunk object stores all packets of one chunk a Replay.
The packets are separated in two types of packets the Player and Server packets.
The difference between player and server packets is that player packets must be processed for each individual player, 
whereas with server packets, the packets must be processed only for the server and for no one else. 

### `addServerPacket(int tick, PacketContainer wrapper)`
Adds a server packet for the given tick to the chunk.
If packets are not added by the system but manually by someone else, this can cause problems, because it can happen that an already existing tick is overwritten and thus all packets already saved for this tick are overwritten.

### `addPacket(UUID uuid, int tick, PacketContainer wrapper)`
Adds a packet for the given uuid and tick to the chunk.
If packets are not added by the system but manually by someone else, this can cause problems, because it can happen that an already existing tick is overwritten and thus all packets already saved for this tick are overwritten.

### `compressData()`
Compress all saved packets and returns it as byte array.

### `getPackets(UUID uuid)`
Returns all Packets saved in the chunk for the give UUID as Map.

### `getServerPackets(int uuid)`
Returns all server packets saved in the chunk for the give tick as list.

### `getPlayers()`
Returns a Collection of all players in the chunk.





