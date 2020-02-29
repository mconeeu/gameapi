package eu.mcone.gameapi.api.replay.npc;

import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.Player;

public interface ReplayNpcManager {

    PlayerNpc createNpc(final ReplayPlayer replayPlayer, final CoreLocation spawnLocation, final Player... players);

    PlayerNpc getNpc(final String npcName);

    ReplayPlayer getReplayPlayer(final String npcName);
}
