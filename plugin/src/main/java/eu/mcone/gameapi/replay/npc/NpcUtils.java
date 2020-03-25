package eu.mcone.gameapi.replay.npc;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.NpcData;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.EntityType;

public class NpcUtils {

    public static PlayerNpc constructNpcForPlayer(final ReplayPlayer player, final String replayID) {
        return (PlayerNpc) CoreSystem.getInstance().getNpcManager().addNPC(new NpcData(
                        EntityType.PLAYER,
                        replayID + "_" + player.getData().getName(),
                        (player.getData().isReported() ? "§8[§cR§8] " + player.getData().getName() : player.getData().getDisplayName()),
                        player.getData().getSpawnLocation(),
                        new PlayerNpcData
                                (
                                        player.getData().getName(),
                                        //TODO: Insert tab prefix
                                        (player.getData().isReported() ? "§8[§cR§8] " + player.getData().getName() : player.getData().getDisplayName()),
                                        SkinInfo.SkinType.PLAYER,
                                        false,
                                        false,
                                        false,
                                        null
                                )
                )
        );
    }
}