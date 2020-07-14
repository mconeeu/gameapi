package eu.mcone.gameapi.replay.npc;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.NpcData;
import eu.mcone.coresystem.api.bukkit.npc.data.PlayerNpcData;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.spawnable.ListMode;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NpcUtils {

    public static PlayerNpc constructNpcForPlayer(final ReplayPlayer player, final UUID containerUUID, Player... watchers) {
        return (PlayerNpc) CoreSystem.getInstance().getNpcManager().addNPC(new NpcData(
                        EntityType.PLAYER,
                        containerUUID + "_" + player.getDisplayName(),
//                        (player.getData().isReported() ? "§8[§cR§8] " + player.getData().getName() : player.getData().getDisplayName()),
                        (player.isReported() ? "§8[§cR§8] " + player.getDisplayName() : player.getDisplayName()),
                        player.getSpawnLocation(),
                        new PlayerNpcData
                                (
                                        player.getDisplayName(),
                                        //TODO: Insert tab prefix
                                        (player.isReported() ? "§8[§cR§8] " + player.getDisplayName() : player.getDisplayName()),
                                        SkinInfo.SkinType.PLAYER,
                                        false,
                                        false,
                                        false,
                                        null
                                )
                ), ListMode.WHITELIST, watchers
        );
    }
}