package eu.mcone.gameapi.replay.npc;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.NpcData;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class ReplayNpcManager implements Listener, eu.mcone.gameapi.api.replay.npc.ReplayNpcManager {

    private final Map<PlayerNpc, ReplayPlayer> npcs;

    public ReplayNpcManager() {
        npcs = new HashMap<>();
    }

    public PlayerNpc createNpc(final ReplayPlayer replayPlayer, final CoreLocation spawnLocation, final Player... players) {
        if (!npcs.containsValue(replayPlayer)) {
            PlayerNpc npc = (PlayerNpc) CoreSystem.getInstance().getNpcManager().addNPC(new NpcData(
                            EntityType.PLAYER,
                            replayPlayer.getUuid().toString(),
                            (replayPlayer.getData().isReported() ? "§8[§cR§8] " + replayPlayer.getData().getDisplayName() : replayPlayer.getData().getDisplayName()),
                            spawnLocation,
                            new PlayerNpcData
                                    (
                                            //replayPlayer.getData().getSkinInfo().getName() != null ? replayPlayer.getData().getSkinInfo().getName() : "bed",
                                            replayPlayer.getData().getName(),
                                            //TODO: Insert tab prefix
                                            (replayPlayer.getData().isReported() ? "§8[§cR§8] " + replayPlayer.getData().getDisplayName() : replayPlayer.getData().getDisplayName()),
                                            SkinInfo.SkinType.PLAYER,
                                            true,
                                            false,
                                            false,
                                            null
                                    )
                    )
            );

            npcs.put(npc, replayPlayer);

            return npc;
        }

        return null;
    }

    public PlayerNpc getNpc(final String npcName) {
        for (Map.Entry<PlayerNpc, ReplayPlayer> entry : npcs.entrySet()) {
            if (entry.getKey().getData().getName().equalsIgnoreCase(npcName)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public ReplayPlayer getReplayPlayer(final String npcName) {
        for (Map.Entry<PlayerNpc, ReplayPlayer> entry : npcs.entrySet()) {
            if (entry.getKey().getData().getName().equalsIgnoreCase(npcName)) {
                return entry.getValue();
            }
        }

        return null;
    }
}